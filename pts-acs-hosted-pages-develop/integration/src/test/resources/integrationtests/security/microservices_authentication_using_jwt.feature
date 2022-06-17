Feature: Authenticate access to a microservice using a JWT
  Attempt to access a microservice using a JWT.  These tests all use the Get Profile using Secure Link service.

  Scenario: The request contains no Services Token
    A request containing no JWT is rejected

    Given a request to the Microservices API
    And the request has no Services Token
    When the request is submitted
    Then it returns an Unauthorized response

  Scenario: The request contains an invalid Services Token
    A request contains a Services Token which cannot be parsed

    Given a request to the Microservices API
    And the request contains a Services Token which 'is invalid'
    When the request is submitted
    Then it returns an Unauthorized response

  Scenario: The request contains a disabled Services Token
    A request contains a Services Token that is disabled

    Given a request to the Microservices API
    And the request contains a Services Token which 'is disabled'
    When the request is submitted
    Then it returns an Unauthorized response

  Scenario: The request contains a Services Token which is not yet effective
    The Services Token's nbf (not before) claim is in the future

    Given a request to the Microservices API
    And the request contains a Services Token which 'is not yet effective'
    When the request is submitted
    Then it returns an Unauthorized response

  Scenario: The request contains a Services Token which is expired
    The Services Token's exp (expiration time) claim is in the past

    Given a request to the Microservices API
    And the request contains a Services Token which 'is expired'
    When the request is submitted
    Then it returns an Unauthorized response

  Scenario: The request contains a Services Token with an unknown company
    The company encoded in the Services Token's FormClaim is unknown

    Given a request to the Microservices API
    And the request contains a Services Token which 'uses an incorrect company'
    When the request is submitted
    Then it returns an Unauthorized response

  Scenario: The request contains a Services Token with an unknown Form UUID
    The Form UUID encoded in the Services Token's FormClaim is unknown

    Given a request to the Microservices API
    And the request contains a Services Token which 'uses an unknown Form UUID'
    When the request is submitted
    Then it returns an Unauthorized response

  Scenario: The request contains a Services Token with a disabled FormConfig
    The FormConfig identified by the Service Token's FormClaim is disabled

    Given a request to the Microservices API
    And the request contains a Services Token which 'uses a disabled Form UUID'
    When the request is submitted
    Then it returns an Unauthorized response

  Scenario: The request contains a Services Token with a FormConfig that is not yet effective
    The EffectiveFrom date of FormConfig identified by the Service Token's FormClaim is in the future
    NB. It is possible that this condition is "unreachable" due to the earlier test of the JWT's nbf claim.

    Given a request to the Microservices API
    And the request contains a Services Token which 'uses a FormConfig Effective From in the future'
    When the request is submitted
    Then it returns an Unauthorized response

  Scenario: The request contains a Services Token with a FormConfig that is no longer effective
    The EffectiveTo date of FormConfig identified by the Service Token's FormClaim is in the past
    NB. It is possible that this condition is "unreachable" due to the earlier test of the JWT's exp claim.

    Given a request to the Microservices API
    And the request contains a Services Token which 'uses a FormConfig Effective To in the past'
    When the request is submitted
    Then it returns an Unauthorized response

  Scenario: The request contains a Services Token where the Token UUID is not attached to the Form UUID
    The Token UUID encoded in the ServiceToken's FormClaim is not linked to the FormConfig UUID in the FormClaim

    Given a request to the Microservices API
    And the request contains a Services Token which 'has mismatched FormConfig and Token UUIDs'
    When the request is submitted
    Then it returns an Unauthorized response

  Scenario: The domain from which the request is submitted is not included in the FormConfig's permitted domain list
    Each form configuration contains a list of permitted domains.  The domain derived from the request is not included
    in this list.

    Given a request to the Microservices API
    And the request contains a Services Token which 'has a FormConfig which does not include the request domain'
    When the request is submitted
    Then it returns an Unauthorized response

  Scenario: The form config identified by the Services Token's FormClaim does not include the role required to use the
  requested Microservices endpoint.
    Each form configuration contains a list of roles the form requires.  Each Microservices endpoint/services layer
    method specifies which role(s) are required to use it.  In this scenario the role required by the services method is
    not present on the form configuration.

    Given a request to the Microservices API
    And the request contains a Services Token which 'has a FormConfig that does not include the role for the requested service'
    When the request is submitted
    Then it returns an Unauthorized response

  Scenario: The request contains a Services Token which passes all the authentication tests

    Given a request to the Microservices API
    And the request contains a Services Token which 'is valid'
    When the request is submitted
    Then it returns a Success response


Feature: Get a profile using a secure link
  Attempt to load a profile using a secure link

  Scenario: The secure link contains an unexpected URL parameter
    In this scenario the secure link contains a URL parameter that is not recognised as either the lookup field
    or the security field

    Given a secure link that contains an unrecognised URL parameter
    When the get profile service is called
    Then it returns a bad request error

  Scenario: The secure link does not contain a lookup key parameter
    In this scenario the secure link is missing the lookup key parameter

    Given a secure link that does not contain a lookup field parameter
    When the get profile service is called
    Then it returns a bad request error

  Scenario: The secure link does not contain a security field parameter
    In this scenario the secure link is missing the security field parameter

    Given a secure link that contains a lookup field parameter
    And the secure link does not contain a security field parameter
    When the get profile service is called
    Then it returns a bad request error

  Scenario: The secure link contains an invalid view parameter

    Given a secure link that contains a lookup field parameter
    And the secure link's security field matches the profile's security field
    And the secure link contains an invalid view parameter
    When the get profile service is called
    Then it returns a bad request error


  Scenario: The lookup field does not match a profile
    In this scenario the secure link contains a lookup field that does not match a profile

    Given a secure link that contains an unknown lookup field
    When the get profile service is called
    Then it returns a resource not found error

  Scenario: The security field does not match the requested profile
    In this the secure link contains a valid lookup field but the given security field does not match the security
    field for the profile

    Given a secure link that contains a valid lookup field
    And the secure link's security field does not match the security field on the retrieved profile
    When the get profile service is called
    Then it returns a resource not found error

  Scenario: The lookup key parameter matches a profile and the security field parameter matches the requested profile
    In this scenario the lookup key value matches an existing profile and the provided security field value matches
    the security field value on the profile and the profile response contains only profile data

    Given a secure link that contains a valid lookup field
    And the secure link's security field matches the profile's security field
    And the view parameter is not provided
    When the get profile service is called
    Then the profile response contains the profile data

  Scenario: The lookup key parameter matches a profile and the security field parameter matches the requested profile
  and the view parameter is "profile"
  In this scenario the lookup key value matches an existing profile and the provided security field value matches
  the security field value on the profile and the profile response contains only profile data

    Given a secure link that contains a valid lookup field
    And the secure link's security field matches the profile's security field
    And the view parameter is profile
    When the get profile service is called
    Then the profile response contains the profile data

  Scenario: The lookup key parameter matches a profile and the security field parameter matches the requested profile
  and the view parameter is "services"
  In this scenario the lookup key value matches an existing profile and the provided security field value matches
  the security field value on the profile and the profile response contains profile data and the current services
  for the profile

    Given a secure link that contains a valid lookup field
    And the secure link's security field matches the profile's security field
    And the view parameter is services
    When the get profile service is called
    Then the profile response contains the profile data
    And the profile response contains a list of current services for the profile

  Scenario: The lookup key parameter matches a profile and the security field parameter matches the requested profile
  and the view parameter is "full"
  In this scenario the lookup key value matches an existing profile and the provided security field value matches
  the security field value on the profile and the profile response contains only profile data

    Given a secure link that contains a valid lookup field
    And the secure link's security field matches the profile's security field
    And the view parameter is full
    When the get profile service is called
    Then the profile response contains the profile data
    And the profile response contains a list of current services for the profile
    And the profile response contains the services the profile has unsubscribed from

Feature: Subscribe and Unsubscribe to services

  Scenario: Update services request contains no PKey
    Given no PKey provided in the update services request uri
    When the update services service is called
    Then the update services service response has status code: 404 NOT FOUND

  Scenario: Update services request contains invalid PKey
    Given invalid PKey provided in the update services request uri
    When the update services service is called
    Then a bad update services request error is returned

  Scenario: Update services request contains no service arrays
    Given valid customerIdHash provided in the update services request uri
    Given update services request contains no service arrays
    When the update services service is called
    Then a bad update services request error is returned

  Scenario: Update services request contains no service names
    Given valid customerIdHash provided in the update services request uri
    Given update services request contains no service names
    When the update services service is called
    Then a bad update services request error is returned

  Scenario: Update services request contains invalid service names
    Given valid customerIdHash provided in the update services request uri
    Given update services request contains invalid service names
    When the update services service is called
    Then a bad update services request error is returned

  Scenario: Update services request passes all validation
    Given valid customerIdHash provided in the update services request uri
    Given all validation is passed for the update services request
    When the update services service is called
    Then the update services service response has status code: 200 OK


#    TODO more permutations
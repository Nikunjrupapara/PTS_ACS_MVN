Feature: Create a profile via a POST request using email address

  Scenario: Create Profile POST request contains empty email field
    Given no value is provided for email in the request
    And the create profile request contains the correct create profile form token
    When the create profile service is called
    Then a no value was provided create profile request error is returned

  Scenario: Create Profile POST request contains an email value
    Given the request body contains a valid email value
    And the create profile request contains the correct create profile form token
    And the create profile request contains custom field cusCustomerId
    When the create profile service is called
    Then the create profile service response has status code: 201 CREATED
    And the create profile response contains the profile data

  Scenario: Create Profile POST request contains an existing email value
    Given the request body contains an existing email value
    And the create profile request contains custom field cusCustomerId
    And the create profile request contains the correct create profile form token
    When the create profile service is called
    Then a bad create profile request error is returned

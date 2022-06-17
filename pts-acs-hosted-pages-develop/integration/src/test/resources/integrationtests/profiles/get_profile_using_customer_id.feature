Feature: Get a profile via a POST request using a customer id
  Attempt to load a profile using a customer id

  Scenario: The POST request contains an invalid view parameter

    Given the POST request contains a view parameter
    And the view param is invalid
    And the search fields list contains and matches all required fields with values
    When the get profile by customer id service is called
    Then a bad request error is returned


  Scenario: The POST request contains an empty list of search fields

    Given the POST request contains an empty list of search fields
    When the get profile by customer id service is called
    Then a supplied payload mismatch error is returned


  Scenario: The size of the search fields list does not match that of the customer definition

    Given the size of the search fields list does not match that of the customer definition
    When the get profile by customer id service is called
    Then a supplied payload mismatch error is returned


  Scenario: The search fields list has one or more fields that are not matched to a customer definition field

    Given the search fields list has one or more fields that are not matched to a customer definition field
    When the get profile by customer id service is called
    Then a supplied payload mismatch error is returned


  Scenario: The search fields list contains required fields without values

    Given the search fields list contains required fields without values
    When the get profile by customer id service is called
    Then a no value supplied in payload error is returned


  Scenario: The search fields list does not match a profile

    Given the search fields list does not match a profile
    When the get profile by customer id service is called
    Then a resource not found error is returned


  Scenario: The search fields list contains and matches all required fields with values and the search field list matches a profile

    Given the search fields list contains and matches all required fields with values
    And the search fields list matches a profile
    And the view param is not provided
    When the get profile by customer id service is called
    Then the profile response has the profile data


  Scenario: The search fields list contains and matches all required fields with values and the search field list matches a profile and the view parameter is "profile"

    Given the search fields list contains and matches all required fields with values
    And the search fields list matches a profile
    And the view param is profile
    When the get profile by customer id service is called
    Then the profile response has the profile data


  Scenario: The search fields list contains and matches all required fields with values and the search field list matches a profile and the view parameter is "services"

    Given the search fields list contains and matches all required fields with values
    And the search fields list matches a profile
    And the view param is services
    When the get profile by customer id service is called
    Then the profile response has a list of current services for the profile


  Scenario: The search fields list contains and matches all required fields with values and the search field list matches a profile and the view parameter is "full"

    Given the search fields list contains and matches all required fields with values
    And the search fields list matches a profile
    And the view param is full
    When the get profile by customer id service is called
    Then the profile response has the profile data
    And the profile response has a list of current services for the profile
    And the profile response has the services the profile has unsubscribed from















Feature: Update Profile

  Scenario: In this scenario, case A, we are updating existing profile based on the supplied data
   This request should return update confirmation with correct status, it will not return user profile.

    Given an update request link that returns a profile object
    And the link contains a valid customerid hash
    And the update profile request contains the correct update profile form token
    And the request contains an update to first name for case A
    And the request contains an update to last name for case A
    When the update profile service is called
    Then the update profile service response has status code: 200 OK
    And response contains PKey in the body

  Scenario: In this scenario, case B, we are updating existing profile based on the supplied data
  This request should return update confirmation with correct status, it will return user profile.

    Given an update request link that returns a profile object
    And the link contains a valid customerid hash
    And the update profile request contains the correct update profile form token
    And the request contains an update to first name for case B
    And the request contains an update to last name for case B
    And the request contains an update to cusFloatNumber for case B
    When the update profile service is called
    Then the update profile service response has status code: 200 OK
    And response contains updated first name for case B
    And response contains updated last name for case B

  Scenario: If supplied Pkey does not exist in ACS then we should get resource not found

    Given an update request link that does not return a profile object
    And the link contains an invalid PKey
    And the update profile request contains the correct update profile form token
    And the request contains an update to first name for case A
    And the request contains an update to last name for case A
    When the update profile service is called
    Then the update profile service response has status code: 404 NOT FOUND

  Scenario: If access to update profile is disabled, update form request should fail

     Given an update request link that does not return a profile object
    And the link contains a valid customerid hash
     And the request contains a token that does not authorize the update
     And the request contains an update to first name for case B
     When the update profile service is called
     Then update request fails authorization

  Scenario: If empty parameter is passed, standard or custom field should update ACS field to empty

    Given an update request link that returns a profile object
    And the link contains a valid customerid hash
    And the update profile request contains the correct update profile form token
    And request contains empty value for first name
#    And request contains empty value for custom field cusFloatNumber
#     TODO: Fix jackson casting float to double bug
    When the update profile service is called
    Then the update profile service response has status code: 200 OK
    And response contains empty first name
#    And response contains empty custom field cusFloatNumber

  Scenario: If we are trying to update fields with non existing standard or custom attribute(s), request should fail. It should return a proper error, and none of the fields should be updated

      Given an update request link that returns a profile object
      And the link contains a valid customerid hash
      And the update profile request contains the correct update profile form token
      And the request contains an update to first name for case B
      And the request contains an update to last name for case B
      And the request contains a non existing attribute
      When the update profile service is called
      Then update profile service returns response with a status code for a bad request
#      And update profile response contains a proper error message for a non existing attribute

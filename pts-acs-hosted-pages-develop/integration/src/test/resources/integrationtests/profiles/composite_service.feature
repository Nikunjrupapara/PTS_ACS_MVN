Feature: Process a composite service

#  BY PROFILE ##################################################################

  Scenario: Process a composite profile service request
    Given a composite profile service request
    When the composite service update or create request is called
    Then the composite service response has status code: 200 OK

  Scenario: Process a composite profile_subscription service request
    Given a composite profile_subscription service request
    When the composite service update or create request is called
    Then the composite service response has status code: 200 OK

  Scenario: Process a composite profile_email service request
    Given a composite profile_email service request
    When the composite service update or create request is called
    Then the composite service response has status code: 200 OK

  Scenario: Process a composite profile_subscription_email service request
    Given a composite profile_subscription_email service request
    When the composite service update or create request is called
    Then the composite service response has status code: 200 OK

#    BY HASH ###################################################################



  Scenario: Process a composite profile by hash service request
    Given a composite profile by hash service request
    When the composite service update by hash request is called
    Then the composite service response has status code: 200 OK

  Scenario: Process a composite profile_subscription by hash service request
    Given a composite profile_subscription by hash service request
    When the composite service update by hash request is called
    Then the composite service response has status code: 200 OK

  @single_run
  Scenario: Process a composite profile_email by hash service request
    Given a composite profile_email by hash service request
    When the composite service update by hash request is called
    Then the composite service response has status code: 200 OK

  Scenario: Process a composite profile_subscription_email by hash service request
    Given a composite profile_subscription_email by hash service request
    When the composite service update by hash request is called
    Then the composite service response has status code: 200 OK

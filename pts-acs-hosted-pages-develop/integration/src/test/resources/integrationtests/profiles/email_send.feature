Feature: Trigger a transaction email request via a POST request

  Scenario: Transactional email request contains no transactional event id
    Given no transactional event id is provided in the request
    When the transactional email service is called
    Then a bad transactional email request error is returned

  Scenario: Transactional email request contains no email address
    Given no email address is provided in the transactional email request
    When the transactional email service is called
    Then a bad transactional email request error is returned

  Scenario: Transactional email request contains no event context
    Given no event context is provided in the transactional email request
    When the transactional email service is called
    Then a bad transactional email request error is returned

  Scenario: Transactional email request passes all validation
    Given all validation is passed for the transactional email request
    When the transactional email service is called
    Then the transactional email service response has status code: 200 OK

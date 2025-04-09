Feature: Test of member registration for basketball England

  Scenario: Register a member with correct data
    Given I go to the webpage "https://membership.basketballengland.co.uk/NewSupporterAccount"
    When I create an account with correct data
    Then I will receive the message "THANK YOU FOR CREATING AN ACCOUNT WITH BASKETBALL ENGLAND"

  Scenario: Register a member with no last name
    Given I go to the webpage "https://membership.basketballengland.co.uk/NewSupporterAccount"
    When I create an account without filling out "last name"
    Then I will receive the message "Last Name is required" for the field "last name"

  Scenario: Register a member without approving terms and conditions
    Given I go to the webpage "https://membership.basketballengland.co.uk/NewSupporterAccount"
    When I create an account without filling out "terms and conditions"
    Then I will receive the message "You must confirm that you have read and accepted our Terms and Conditions" for the field "terms and conditions"

  Scenario: Register a member with unmatching data for confirm email
    Given I go to the webpage "https://membership.basketballengland.co.uk/NewSupporterAccount"
    When I create an account with unmatching data for "confirm email address"
    Then I will receive the message "Confirm Email Address does not match" for the field "confirm email address"

  Scenario Outline: Register a member with incorrect data
    Given I use the <browser>
    And I go to the webpage "https://membership.basketballengland.co.uk/NewSupporterAccount"
    When I create an account with the incorrect <value> for <field>
    Then I will receive the <message> for <field>

    Examples:
      | browser      | field                   | value           | message                                                                                       |
      | "chrome"     | "first name"            | "!!!"           | "Invalid Value - Allowed characters: A-Z, space and '/\&.-"                                   |
      | "edge"       | "ethics and conduct"    | "empty"         | "You must confirm that you have read, understood and agree to the Code of Ethics and Conduct" |
      | "duckduckgo" | "confirm email address" | "mail@mail.com" | "Confirm Email Address does not match"                                                        |


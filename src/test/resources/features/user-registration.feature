Feature: Test of member registration for basketball England

  Scenario: Register a member with correct data
    Given I go to the webpage "https://membership.basketballengland.co.uk/NewSupporterAccount"
    When I create an account with correct data
    Then I will receive the message "THANK YOU FOR CREATING AN ACCOUNT WITH BASKETBALL ENGLAND"

  Scenario Outline: Register a member with incorrect or empty data
    Given I go to the webpage "https://membership.basketballengland.co.uk/NewSupporterAccount"
    When I create an account with the incorrect or empty <value> for <field>
    Then I will receive the <message> for <field>

    Examples:
      | field                   | value           | message                                                                     |
      | "last name"             | " "             | "Last Name is required"                                                     |
      | "terms and conditions"  | "empty"         | "You must confirm that you have read and accepted our Terms and Conditions" |
      | "confirm email address" | "mail@mail.com" | "Confirm Email Address does not match"                                      |


  Scenario Outline: Run same registration test on different browsers
    Given I use the <browser>
    Given I go to the webpage "https://membership.basketballengland.co.uk/NewSupporterAccount"
    When I create an account with the incorrect or empty <value> for <field>
    Then I will receive the <message> for <field>

    Examples:
      | browser      | field                | value   | message                                                                                       |
      | "chrome"     | "ethics and conduct" | "empty" | "You must confirm that you have read, understood and agree to the Code of Ethics and Conduct" |
      | "edge"       | "ethics and conduct" | "empty" | "You must confirm that you have read, understood and agree to the Code of Ethics and Conduct" |
      | "duckduckgo" | "ethics and conduct" | "empty" | "You must confirm that you have read, understood and agree to the Code of Ethics and Conduct" |


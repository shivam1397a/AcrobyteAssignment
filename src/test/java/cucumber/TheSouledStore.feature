Feature: TheSouledStore

  Scenario: Validate that user is able to add last available product to their cart
    Given I navigate to TheSouledStore home page in "chrome" browser
    And I select "t-shirts" tile under "Categories"
    And I select last available product
    And I select "S" size and "02" quantity
    And I click on "Add to cart" button
    When I click on "Go to cart" link
    Then I verify that product is added successfully
    And I close the automation browser
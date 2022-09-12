Feature: Brew Functionality

  @api
  Scenario Outline: User should be able to see correct beer style infos
    When User search a valid "<Beer Styles>"
    Then Status code should be 200
    And User should see matching infos
    And Response should match with json schema

    Examples:
      | Beer Styles         |
      | Lite American Lager |
      | London Brown Ale    |
      | Czech Pale Lager    |

  @api
  Scenario Outline: User should not be able to see invalid beer style infos
    When User search a valid "<Beer Styles>"
    Then status code should not be 200

    Examples:
      | Beer Styles            |
      | Lite American Lager123 |
      | American Lager 2       |
      | Czech Pale Lager21     |

  @api
  Scenario Outline: User should be able to calculate alcohol by volume (abv) in percentage from
  final and original gravity
    When User posts "<og>" and "<fg>"
    Then Status code should be 200
    And "Content-Type" should be "application/json"
    And User should see correct abv result

    Examples:
      | og    | fg    |
      | 1.066 | 1.014 |
      | 1.080 | 1.007 |


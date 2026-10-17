@cars
Feature: Filters Our Cars

  Background:
    Given the user goes to the cars page on Revel

  Scenario Outline: Verify response of the filter "<filter>" option "<option>"
    When the user clicks on filter "<filter>"
    And the user chooses the option "<option>"
    Then the filter option "<filterOptionInURL>" appears in the URL
    And the first car in the results page is "<firstCarModel>" "<firstCarVersion>"

    @filter-fuel-electric-hybrid
    Examples: Fuel Electric Hybrid
      | filter | option          | filterOptionInURL          | firstCarModel | firstCarVersion |
      | Fuel   | Electric Hybrid | ?fuelTypes=electric-hybrid | Toyota C-HR   |   220PH Advance |

    @filter-body-type-suv
    Examples: Body Type SUV
      | filter    | option | filterOptionInURL | firstCarModel | firstCarVersion |
      | Body Type | SUV    | ?bodyType=suv     | Kia XCeed     | 1.0 T-GDi Drive |

  @filter-combined-filters
  Scenario Outline: Verify response of the filters "<filter_1>" option "<option_1>" and "<filter_2>" option "<option_2>"
    When the user clicks on filter "<filter_1>"
    And the user chooses the option "<option_1>"
    And the user clicks on filter "<filter_2>"
    And the user chooses the option "<option_2>"
    Then the filter option "<filtersOptionsInURL>" appears in the URL
    And the first car in the results page is "<firstCarModel>" "<firstCarVersion>"

    Examples:
      | filter_1 | option_1        | filter_2  | option_2 | filtersOptionsInURL                     | firstCarModel | firstCarVersion |
      | Fuel     | Electric Hybrid | Body Type | SUV      | ?fuelTypes=electric-hybrid&bodyType=suv | Toyota C-HR   |   220PH Advance |

  @filter-no-results
  Scenario Outline: Filters without results: "<filter_1>: <option_1>", "<filter_2>: <option_2>", "<filter_3>: <option_3>"
    When the user clicks on filter "<filter_1>"
    And the user chooses the option "<option_1>"
    And the user clicks on filter "<filter_2>"
    And the user chooses the option "<option_2>"
    And the user clicks on filter "<filter_3>"
    And the user chooses the option "<option_3>"
    Then the filter option "<filtersOptionsInURL>" appears in the URL
    And a message appears indicating that there are no results for the selected filters

    Examples:
      | filter_1 | option_1        | filter_2  | option_2 | filter_3 | option_3 | filtersOptionsInURL                                 |
      | Fuel     | Electric Hybrid | Body Type | SUV      | Color    | White    | ?fuelTypes=electric-hybrid&bodyType=suv&color=white |

  @filter-reset
  Scenario Outline: Cleaning filters "<filter_1>: <option_1>" and "<filter_2>: <option_2>"
    When the user clicks on filter "<filter_1>"
    And the user chooses the option "<option_1>"
    And the user clicks on filter "<filter_2>"
    And the user chooses the option "<option_2>"
    And the user cleans the filter "<filter_1>"
    And the user cleans the filter "<filter_2>"
    Then the URL does not contain any filter options
    And the first car in the results page is "<firstCarModel>" "<firstCarVersion>"

    Examples:
      | filter_1 | option_1        | filter_2  | option_2 | firstCarModel | firstCarVersion |
      | Fuel     | Electric Hybrid | Body Type | SUV      | Peugeot 208   | Allure          |

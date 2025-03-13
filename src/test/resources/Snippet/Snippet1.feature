Feature: Snippet Demo Feature

  Scenario Outline: User login to M2U
    Then print string "<username>" 123.00 true
    Then print string "<password>" 345.00 false
    Then print string "<test data1>" 12.00 false
    Examples:
    |username|password|test data1|
    |hakim   | abc    | rm200    |
    |moh     | 123    | rm100    |
    |zxc     | vbn    | ghj    |


  Scenario: Verify Snippet Code Is Running
    Then print from data table without header below
      |"123"|"abc"|
      |"456"|"def"|
    Then print from data table without header below
      |"111"|"zzz"|
      |"444"|"fff"|
    Then print from data table with header below
      |"name"|"age"|
      | "Ali"|12   |
      | "Abu"|14   |






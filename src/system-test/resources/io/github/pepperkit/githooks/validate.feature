Feature: Print installed git hooks via validate goal
  Validate goal provides the ability to print all or specific hooks installed at the moment, to make sure that the
  plugin was configured correctly.

  Scenario: Prints all the hooks configured and installed
    Given there's a maven project with git-hooks plugin configured
    And git repository is set up for the project
    And init goal was launched before with hooks presented in configuration
    When validate goal of plugin is launched without specifying a particular hook
    Then it prints all the hooks installed at the moment

  Scenario: Prints warning message if no hooks are configured
    Given there's a maven project with git-hooks plugin configured
    And git repository is set up for the project
    And init goal was launched before with no hooks presented in configuration
    When validate goal of plugin is launched without specifying a particular hook
    Then it prints that no hooks were configured

  Scenario: Prints only the specified hook
    Given there's a maven project with git-hooks plugin configured
    And git repository is set up for the project
    And init goal was launched before with the specified hook presented in configuration
    When validate goal of plugin is launched with the specific hook name provided
    Then it prints only this hook

  Scenario: Throws hook is not installed error if specified hook is not installed
    Given there's a maven project with git-hooks plugin configured
    And git repository is set up for the project
    And init goal was launched before with the specified hook not presented in configuration
    When validate goal of plugin is launched with the specific hook name provided
    Then it throws hook is not installed error


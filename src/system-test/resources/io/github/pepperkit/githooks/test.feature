Feature: Execute installed git hooks
  executeHooks goal provides the ability to execute all or specific hooks installed at the moment, to make sure that the
  hooks work as expected, without the need to actually trigger the hook with git action.

  Scenario: Execute all the hooks configured and installed
    Given there's a maven project with git-hooks plugin configured
    And git repository is set up for the project
    And initHooks goal was launched before with hooks presented in configuration
    When executeHooks goal of plugin is launched without specifying a particular hook
    Then it executes all the hooks installed at the moment

  Scenario: Prints warning message if no hooks are configured
    Given there's a maven project with git-hooks plugin configured
    And git repository is set up for the project
    And initHooks goal was launched before with no hooks presented in configuration
    When executeHooks goal of plugin is launched without specifying a particular hook
    Then it prints that no hooks were configured

  Scenario: Execute only the specified hook
    Given there's a maven project with git-hooks plugin configured
    And git repository is set up for the project
    And initHooks goal was launched before with the specified hook presented in configuration
    When executeHooks goal of plugin is launched with the specific hook name provided
    Then it executes only this hook

  Scenario: Throws hook is not installed error if specified hook is not installed
    Given there's a maven project with git-hooks plugin configured
    And git repository is set up for the project
    And initHooks goal was launched before with the specified hook not presented in configuration
    When executeHooks goal of plugin is launched with the specific hook name provided
    Then it throws hook is not installed error


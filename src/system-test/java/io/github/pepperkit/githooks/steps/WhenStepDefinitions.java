/*
 * Copyright (C) 2021 PepperKit
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package io.github.pepperkit.githooks.steps;

import java.io.IOException;

import io.cucumber.java.en.When;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WhenStepDefinitions extends BaseMojoSysTest {

    @When("init goal of the plugin is launched with hooks presented in plugin's configuration")
    public void initGoalIsLaunchedWithHooks() throws IOException, InterruptedException {
        cmdResult = container.execInContainer("mvn", "-f", "pre_commit_push_hooks-pom.xml",
                "io.github.pepperkit:git-hooks-maven-plugin:init");
    }

    @When("init goal of the plugin is launched with hooks deleted from plugin's configuration")
    @When("init goal of the plugin is launched with another plugin's configuration")
    public void hooksDeletedFromPom() throws IOException, InterruptedException {
        cmdResult = container.execInContainer("mvn", "-f", "no_hooks-pom.xml",
                "io.github.pepperkit:git-hooks-maven-plugin:init");
        assertThat(cmdResult.getStdout())
                .contains("BUILD SUCCESS");
    }

    @When("validate goal of plugin is launched without specifying a particular hook")
    public void validateGoalLaunchedWithoutSpecificHooks() throws IOException, InterruptedException {
        cmdResult = container.execInContainer("mvn", "-f", "pre_commit_push_hooks-pom.xml",
                "io.github.pepperkit:git-hooks-maven-plugin:validate");
    }

    @When("validate goal of plugin is launched with the specific hook name provided")
    public void validateGoalLaunchedWithPreCommitHookSpecified() throws IOException, InterruptedException {
        cmdResult = container.execInContainer("mvn", "-f", "pre_commit_push_hooks-pom.xml",
                "-DhookName=pre-commit", "io.github.pepperkit:git-hooks-maven-plugin:validate");
    }

    @When("test goal of plugin is launched without specifying a particular hook")
    public void testGoalLaunchedWithoutSpecificHooks() throws IOException, InterruptedException {
        cmdResult = container.execInContainer("mvn", "-f", "pre_commit_push_hooks-pom.xml",
                "io.github.pepperkit:git-hooks-maven-plugin:test");
    }

    @When("test goal of plugin is launched with the specific hook name provided")
    public void testGoalLaunchedWithPrePushHookSpecified() throws IOException, InterruptedException {
        cmdResult = container.execInContainer("mvn", "-f", "pre_commit_push_hooks-pom.xml",
                "-DhookName=pre-push", "io.github.pepperkit:git-hooks-maven-plugin:test");
    }
}

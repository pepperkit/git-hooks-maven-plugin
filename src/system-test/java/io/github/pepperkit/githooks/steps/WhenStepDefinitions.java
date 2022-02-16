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

public class WhenStepDefinitions extends BaseMojoSysTest {

    @When("initHooks goal of the plugin is launched with hooks presented in plugin's configuration")
    public void initGoalIsLaunchedWithHooks() throws IOException, InterruptedException {
        cmdResult = container.execInContainer("mvn", "-f", "pre_commit_push_hooks-pom.xml",
                "io.github.pepperkit:git-hooks-maven-plugin:initHooks");
    }

    @When("initHooks goal of the plugin is launched with hooks deleted from plugin's configuration")
    @When("initHooks goal of the plugin is launched with another plugin's configuration")
    public void hooksDeletedFromPom() throws IOException, InterruptedException {
        cmdResult = container.execInContainer("mvn", "-f", "no_hooks-pom.xml",
                "io.github.pepperkit:git-hooks-maven-plugin:initHooks");
        assertThat(cmdResult.getStdout())
                .contains("BUILD SUCCESS");
    }

    @When("printHooks goal of plugin is launched without specifying a particular hook")
    public void validateGoalLaunchedWithoutSpecificHooks() throws IOException, InterruptedException {
        cmdResult = container.execInContainer("mvn", "-f", "pre_commit_push_hooks-pom.xml",
                "io.github.pepperkit:git-hooks-maven-plugin:printHooks");
    }

    @When("printHooks goal of plugin is launched with the specific hook name provided")
    public void validateGoalLaunchedWithPreCommitHookSpecified() throws IOException, InterruptedException {
        cmdResult = container.execInContainer("mvn", "-f", "pre_commit_push_hooks-pom.xml",
                "-DhookName=pre-commit", "io.github.pepperkit:git-hooks-maven-plugin:printHooks");
    }

    @When("executeHooks goal of plugin is launched without specifying a particular hook")
    public void testGoalLaunchedWithoutSpecificHooks() throws IOException, InterruptedException {
        cmdResult = container.execInContainer("mvn", "-f", "pre_commit_push_hooks-pom.xml",
                "io.github.pepperkit:git-hooks-maven-plugin:executeHooks");
    }

    @When("executeHooks goal of plugin is launched with the specific hook name provided")
    public void testGoalLaunchedWithPrePushHookSpecified() throws IOException, InterruptedException {
        cmdResult = container.execInContainer("mvn", "-f", "pre_commit_push_hooks-pom.xml",
                "-DhookName=pre-push", "io.github.pepperkit:git-hooks-maven-plugin:executeHooks");
    }
}

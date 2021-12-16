/*
 * Copyright (C) 2021 PepperKit
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package io.github.pepperkit.githooks.steps;

import java.io.IOException;

import io.cucumber.java.en.Given;

import static org.assertj.core.api.Assertions.assertThat;

public class GivenStepDefinitions extends BaseMojoSysTest {

    @Given("there's a maven project with git-hooks plugin configured")
    public void testProjectWithPluginConfigured() {
        // Project is set in the docker image
    }

    @Given("git repository is not set up for the project")
    public void repoIsNotSetUp() {
        // By default, the repository is not set up for the project
    }

    @Given("git repository is set up for the project")
    public void repoIsSetUp() throws IOException, InterruptedException {
        container.execInContainer("git", "init");
    }

    @Given("init goal was launched before with hooks presented in configuration")
    @Given("init goal was launched before with the specified hook presented in configuration")
    public void initWithHooksConfigured() throws IOException, InterruptedException {
        cmdResult = container.execInContainer("mvn", "-f", "pre_commit_push_hooks-pom.xml",
                "io.github.pepperkit:git-hooks-maven-plugin:init");
        assertThat(cmdResult.getStdout())
                .contains("BUILD SUCCESS");

        cmdResult = container.execInContainer("cat", ".git/hooks/pre-commit");
        assertThat(cmdResult.getStdout())
                .contains("pre-commit hook is invoked");
    }

    @Given("init goal was launched before with no hooks presented in configuration")
    @Given("init goal was launched before with the specified hook not presented in configuration")
    public void initWithNoHooksConfigured() throws IOException, InterruptedException {
        cmdResult = container.execInContainer("mvn", "-f", "no_hooks-pom.xml",
                "io.github.pepperkit:git-hooks-maven-plugin:init");
        assertThat(cmdResult.getStdout())
                .contains("BUILD SUCCESS");
    }
}

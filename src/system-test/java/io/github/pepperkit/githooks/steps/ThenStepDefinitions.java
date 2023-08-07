/*
 * Copyright (C) 2023 PepperKit
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package io.github.pepperkit.githooks.steps;

import java.io.IOException;

import io.cucumber.java.en.Then;

import static org.assertj.core.api.Assertions.assertThat;

public class ThenStepDefinitions extends BaseMojoSysTest {

    @Then("it throws not a git repository error")
    public void throwsNotGitRepoError() {
        assertThat(cmdResult.getStdout())
                .contains("ERROR")
                .contains("not a git repository");
    }

    @Then("these hooks are installed to git correctly")
    public void hooksAreInstalledCorrectly() throws IOException, InterruptedException {
        assertThat(cmdResult.getStdout())
                .contains("pre-commit")
                .contains("pre-push")
                .contains("BUILD SUCCESS");

        cmdResult = container.execInContainer("cat", ".git/hooks/pre-commit");
        assertThat(cmdResult.getStdout())
                .contains("pre-commit hook is invoked")
                .doesNotContain("pre-push hook is invoked");

        cmdResult = container.execInContainer("cat", ".git/hooks/pre-push");
        assertThat(cmdResult.getStdout())
                .contains("pre-push hook is invoked")
                .doesNotContain("pre-commit hook is invoked");
    }

    @Then("previously added hooks are deleted")
    public void previouslyAddedHooksAreDeleted() throws IOException, InterruptedException {
        cmdResult = container.execInContainer("cat", ".git/hooks/pre-commit");
        assertThat(cmdResult.getStdout())
                .doesNotContainIgnoringCase("echo \"pre-commit hook is invoked\"");

        cmdResult = container.execInContainer("cat", ".git/hooks/pre-push");
        assertThat(cmdResult.getStdout())
                .doesNotContainIgnoringCase("echo \"pre-push hook is invoked\"");
    }

    @Then("previously added hooks are archived")
    public void previouslyAddedHooksAreArchived() throws IOException, InterruptedException {
        cmdResult = container.execInContainer("ls", ".git/hooks/archived");
        assertThat(cmdResult.getStdout().split(" "))
                .hasSize(1);
    }

    @Then("previously added hooks are not archived")
    public void previouslyAddedHooksAreNotArchived() throws IOException, InterruptedException {
        cmdResult = container.execInContainer("ls", ".git/hooks/archived");
        assertThat(cmdResult.getStdout().split(" "))
                .hasSize(0);
    }

    @Then("it prints all the hooks installed at the moment")
    public void printsAllHooksInstalled() {
        assertThat(cmdResult.getStdout())
                .contains("`pre-commit` -> The following commands will be invoked: \n"
                        + "#!/bin/sh\n"
                        + "echo \"pre-commit hook is invoked\"")
                .contains("`pre-push` -> The following commands will be invoked: \n"
                        + "#!/bin/sh\n"
                        + "echo \"pre-push hook is invoked\"")
                .contains("BUILD SUCCESS");
    }

    @Then("it prints that no hooks were configured")
    public void printsNoHooksConfigured() {
        assertThat(cmdResult.getStdout())
                .contains("No hooks are configured. Make sure you have correctly configured plugin and "
                        + "ran initHooks goal first to install the hooks.")
                .contains("BUILD SUCCESS");
    }

    @Then("it prints only this hook")
    public void printsOnlyPreCommitHook() {
        assertThat(cmdResult.getStdout())
                .contains("`pre-commit` -> The following commands will be invoked: \n"
                        + "#!/bin/sh\n"
                        + "echo \"pre-commit hook is invoked\"")
                .doesNotContain("`pre-push` -> The following commands will be invoked: \n"
                        + "#!/bin/sh\n"
                        + "echo \"pre-push hook is invoked\"")
                .contains("BUILD SUCCESS");
    }

    @Then("it throws hook is not installed error")
    public void throwsHookIsNotInstalledError() {
        assertThat(cmdResult.getStdout())
                .contains("ERROR")
                .contains("The specified hook")
                .contains("is not installed.");
    }

    @Then("it executes all the hooks installed at the moment")
    public void executesAllHooksInstalled() {
        assertThat(cmdResult.getStdout())
                .contains("pre-commit")
                .contains("pre-push")
                .contains("BUILD SUCCESS");
    }

    @Then("it executes only this hook")
    public void executesOnlyPrePushHook() {
        assertThat(cmdResult.getStdout())
                .contains("pre-push")
                .doesNotContain("pre-commit")
                .contains("BUILD SUCCESS");
    }
}

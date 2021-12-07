/*
 * Copyright (C) 2021 PepperKit
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package io.github.pepperkit.githooks;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InitMojoSysTest extends BaseMojoSysTest {

    @Test
    void initWorksCorrectly() throws IOException, InterruptedException {
        org.testcontainers.containers.Container.ExecResult cmdResult;

        container.execInContainer("git", "init");

        cmdResult = container.execInContainer("mvn", "-f", "pre_commit_push_hooks-pom.xml", "compile");
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

    @Test
    void initReturnsErrorIfNotGitRepository() throws IOException, InterruptedException {
        org.testcontainers.containers.Container.ExecResult cmdResult;

        cmdResult = container.execInContainer("mvn", "-f", "pre_commit_push_hooks-pom.xml", "compile");
        assertThat(cmdResult.getStdout())
                .contains("ERROR")
                .contains("not a git repository");
    }
}

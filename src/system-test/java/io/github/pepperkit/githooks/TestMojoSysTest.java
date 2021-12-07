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

class TestMojoSysTest extends BaseMojoSysTest {

    @Test
    void testWorksCorrectly() throws IOException, InterruptedException {
        org.testcontainers.containers.Container.ExecResult cmdResult;

        container.execInContainer("git", "init");

        cmdResult = container.execInContainer("mvn", "-f", "pre_commit_push_hooks-pom.xml", "verify");
        assertThat(cmdResult.getStdout())
                .contains("pre-commit hook is invoked")
                .contains("pre-push hook is invoked")
                .contains("BUILD SUCCESS");
    }
}

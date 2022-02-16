/*
 * Copyright (C) 2021 PepperKit
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package io.github.pepperkit.githooks.steps;

import java.nio.file.Paths;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.startupcheck.IsRunningStartupCheckStrategy;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class BaseMojoSysTest {

    public static org.testcontainers.containers.Container.ExecResult cmdResult;

    public static GenericContainer<?> container = new GenericContainer<>(
            new ImageFromDockerfile("git-hooks-maven-plugin-test", true)
                    .withFileFromPath(".", Paths.get(".")))
                    .withWorkingDirectory("/test-projects")
                    .withStartupCheckStrategy(new IsRunningStartupCheckStrategy())
                    .withCreateContainerCmdModifier(cmd -> cmd
                            .withStdinOpen(true)
                            .withTty(true)
                            .withCmd("/bin/sh"));
}

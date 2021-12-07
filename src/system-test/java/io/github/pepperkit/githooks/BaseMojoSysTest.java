/*
 * Copyright (C) 2021 PepperKit
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package io.github.pepperkit.githooks;

import java.nio.file.Paths;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.startupcheck.IsRunningStartupCheckStrategy;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class BaseMojoSysTest {

    @Container
    static GenericContainer<?> container = new GenericContainer<>(new ImageFromDockerfile()
            .withFileFromPath(".", Paths.get(".")))
            .withWorkingDirectory("/test-projects")
            .withStartupCheckStrategy(new IsRunningStartupCheckStrategy())
            .withCreateContainerCmdModifier(cmd -> cmd
                    .withStdinOpen(true)
                    .withTty(true)
                    .withCmd("/bin/sh"));
}

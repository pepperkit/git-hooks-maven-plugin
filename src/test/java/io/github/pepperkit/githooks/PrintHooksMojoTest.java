/*
 * Copyright (C) 2021 PepperKit
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package io.github.pepperkit.githooks;

import java.io.IOException;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PrintHooksMojoTest {

    GitHooksManager gitHooksManagerMock;

    PrintHooksMojo printHooksMojo;

    @BeforeEach
    void beforeEach() {
        gitHooksManagerMock = mock(GitHooksManager.class);
        printHooksMojo = new PrintHooksMojo();
        printHooksMojo.gitHooksManager = gitHooksManagerMock;
    }

    @Test
    void validatesAllHooksIfHookNameIsNotProvided() throws MojoExecutionException, IOException {
        printHooksMojo.execute();
        verify(gitHooksManagerMock, times(GitHooksManager.GIT_HOOKS.size())).printHook(any());
    }

    @Test
    void validatesOnlySpecifiedHookIfProvided() throws MojoExecutionException, IOException {
        final String hookToValidate = "pre-commit";
        printHooksMojo.hookName = hookToValidate;

        when(gitHooksManagerMock.printHook(any())).thenReturn(true);

        printHooksMojo.execute();
        verify(gitHooksManagerMock, times(1)).printHook(hookToValidate);
    }

    @Test
    void throwsExceptionIfSpecifiedHookIsNotInstalled() throws IOException {
        printHooksMojo.hookName = "pre-commit";

        when(gitHooksManagerMock.printHook(any())).thenReturn(false);

        MojoExecutionException excThrown = assertThrows(MojoExecutionException.class, printHooksMojo::execute);
        assertThat(excThrown.getMessage()).contains("is not installed");
    }

    @Test
    void validateThrowsMojoExecutionExceptionIfReadingOfHookFails() throws MojoExecutionException, IOException {
        final String hookToValidate = "pre-commit";
        printHooksMojo.hookName = hookToValidate;

        doThrow(new IOException()).when(gitHooksManagerMock).printHook(any());

        MojoExecutionException excThrown = assertThrows(MojoExecutionException.class, printHooksMojo::execute);
        assertThat(excThrown.getMessage()).contains(hookToValidate);
    }
}

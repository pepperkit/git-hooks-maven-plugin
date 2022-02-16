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

class TestMojoTest {

    GitHooksManager gitHooksManagerMock;

    ExecuteHooksMojo executeHooksMojo;

    @BeforeEach
    void beforeEach() {
        gitHooksManagerMock = mock(GitHooksManager.class);
        executeHooksMojo = new ExecuteHooksMojo();
        executeHooksMojo.gitHooksManager = gitHooksManagerMock;
    }

    @Test
    void testsAllHooksIfHookNameIsNotProvided() throws MojoExecutionException, IOException, InterruptedException {
        executeHooksMojo.execute();
        verify(gitHooksManagerMock, times(GitHooksManager.GIT_HOOKS.size())).executeHook(any());
    }

    @Test
    void testsOnlySpecifiedHookIfProvided() throws MojoExecutionException, IOException, InterruptedException {
        final String hookToTest = "pre-commit";
        executeHooksMojo.hookName = hookToTest;

        when(gitHooksManagerMock.executeHook(any())).thenReturn(true);

        executeHooksMojo.execute();
        verify(gitHooksManagerMock, times(1)).executeHook(hookToTest);
    }

    @Test
    void throwsExceptionIfSpecifiedHookIsNotInstalled() throws IOException, InterruptedException {
        executeHooksMojo.hookName = "pre-commit";

        when(gitHooksManagerMock.executeHook(any())).thenReturn(false);

        MojoExecutionException excThrown = assertThrows(MojoExecutionException.class, executeHooksMojo::execute);
        assertThat(excThrown.getMessage()).contains("is not installed");
    }

    @Test
    void testThrowsMojoExecutionExceptionIfIOExceptionIsThrown() throws InterruptedException, IOException {
        final String hookToTest = "pre-commit";
        executeHooksMojo.hookName = hookToTest;

        doThrow(new IOException()).when(gitHooksManagerMock).executeHook(any());

        MojoExecutionException excThrown = assertThrows(MojoExecutionException.class, executeHooksMojo::execute);
        assertThat(excThrown.getMessage()).contains(hookToTest);
    }

    @Test
    void testInterruptsIfHookExecIsInterrupted() throws InterruptedException, IOException, MojoExecutionException {
        doThrow(new InterruptedException()).when(gitHooksManagerMock).executeHook(any());

        executeHooksMojo.execute();
        assertThat(Thread.interrupted()).isTrue();
    }
}
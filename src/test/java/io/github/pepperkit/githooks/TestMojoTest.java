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

    TestMojo testMojo;

    @BeforeEach
    void beforeEach() {
        gitHooksManagerMock = mock(GitHooksManager.class);
        testMojo = new TestMojo();
        testMojo.gitHooksManager = gitHooksManagerMock;
    }

    @Test
    void testsAllHooksIfHookNameIsNotProvided() throws MojoExecutionException, IOException, InterruptedException {
        testMojo.execute();
        verify(gitHooksManagerMock, times(GitHooksManager.GIT_HOOKS.size())).executeHook(any());
    }

    @Test
    void testsOnlySpecifiedHookIfProvided() throws MojoExecutionException, IOException, InterruptedException {
        final String hookToTest = "pre-commit";
        testMojo.hookName = hookToTest;

        when(gitHooksManagerMock.executeHook(any())).thenReturn(true);

        testMojo.execute();
        verify(gitHooksManagerMock, times(1)).executeHook(hookToTest);
    }

    @Test
    void throwsExceptionIfSpecifiedHookIsNotInstalled() throws IOException, InterruptedException {
        testMojo.hookName = "pre-commit";

        when(gitHooksManagerMock.executeHook(any())).thenReturn(false);

        MojoExecutionException excThrown = assertThrows(MojoExecutionException.class, testMojo::execute);
        assertThat(excThrown.getMessage()).contains("is not installed");
    }

    @Test
    void testThrowsMojoExecutionExceptionIfIOExceptionIsThrown() throws InterruptedException, IOException {
        final String hookToTest = "pre-commit";
        testMojo.hookName = hookToTest;

        doThrow(new IOException()).when(gitHooksManagerMock).executeHook(any());

        MojoExecutionException excThrown = assertThrows(MojoExecutionException.class, testMojo::execute);
        assertThat(excThrown.getMessage()).contains(hookToTest);
    }

    @Test
    void testInterruptsIfHookExecIsInterrupted() throws InterruptedException, IOException, MojoExecutionException {
        testMojo.hookName = "pre-commit";

        doThrow(new InterruptedException()).when(gitHooksManagerMock).executeHook(any());

        testMojo.execute(); // todo: check that
    }
}
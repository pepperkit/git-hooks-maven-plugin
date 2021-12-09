package io.github.pepperkit.githooks;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class InitMojoTest {

    GitHooksManager gitHooksManagerMock;

    InitMojo initMojo;

    @BeforeEach
    void beforeEach() {
        gitHooksManagerMock = mock(GitHooksManager.class);
        initMojo = new InitMojo();
        initMojo.gitHooksManager = gitHooksManagerMock;
    }

    @Test
    void executesNothingIfHooksAreNotProvided() throws MojoExecutionException, IOException {
        initMojo.execute();
        verify(gitHooksManagerMock, times(0)).checkProvidedHookNamesCorrectness(any());
        verify(gitHooksManagerMock, times(0)).checkGitHooksDirAndCreateIfMissing();
        verify(gitHooksManagerMock, times(0)).createHook(any(), any());
    }

    @Test
    void createsCorrectHooks() throws MojoExecutionException, IOException {
        Map<String, String> hooks = new HashMap<>();
        hooks.put("pre-commit", "mvn -B checkstyle:checkstyle");
        hooks.put("pre-push", "mvn -B verify");
        initMojo.hooks = hooks;

        initMojo.execute();

        verify(gitHooksManagerMock, times(1)).checkProvidedHookNamesCorrectness(hooks);
        verify(gitHooksManagerMock, times(1)).checkGitHooksDirAndCreateIfMissing();
        verify(gitHooksManagerMock, times(1))
                .createHook("pre-commit", hooks.get("pre-commit"));
        verify(gitHooksManagerMock, times(1))
                .createHook("pre-push", hooks.get("pre-push"));
    }

    @Test
    void initThrowsMojoExecutionExceptionIfCreatingOfHookFails() throws MojoExecutionException, IOException {
        Map<String, String> hooks = new HashMap<>();
        hooks.put("pre-commit", "mvn -B checkstyle:checkstyle");
        hooks.put("pre-push", "mvn -B verify");
        initMojo.hooks = hooks;

        initMojo.execute();

        doThrow(new IOException()).when(gitHooksManagerMock)
                .createHook("pre-push", hooks.get("pre-push"));

        MojoExecutionException excThrown = assertThrows(MojoExecutionException.class, initMojo::execute);
        assertThat(excThrown.getMessage()).contains("pre-push");
    }
}
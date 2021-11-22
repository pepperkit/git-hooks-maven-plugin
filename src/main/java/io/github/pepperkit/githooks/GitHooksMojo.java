package io.github.pepperkit.githooks;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.*;

@Mojo(name = "git-hooks", defaultPhase = LifecyclePhase.INITIALIZE)
public class GitHooksMojo extends AbstractMojo {

    private static final String PLUGIN_NAME = "git-hooks-maven-plugin";

    private static final Path GIT_PATH = Paths.get(".git");

    private static final Path GIT_HOOKS_PATH = Paths.get(".git/hooks");

    private static final Set<String> GIT_HOOKS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            "applypatch-msg",
            "commit-msg",
            "fsmonitor-watchman",
            "post-update",
            "pre-applypatch",
            "pre-commit",
            "pre-merge-commit",
            "pre-push",
            "pre-rebase",
            "pre-receive",
            "prepare-commit-msg",
            "push-to-checkout",
            "update"
    )));

    private static final Set<PosixFilePermission> HOOK_FILE_PERMISSIONS = Collections.unmodifiableSet(
            new HashSet<>(Arrays.asList(
                    PosixFilePermission.OWNER_READ,
                    PosixFilePermission.OWNER_WRITE,
                    PosixFilePermission.OWNER_EXECUTE
            )));

    @Parameter(defaultValue = "null")
    public Map<String, String> hooks;

    @Parameter(defaultValue = "false")
    public boolean alwaysOverride;

    public void setHooks(String hooks) {
        if (hooks.equals("null")) {
            this.hooks = null;
        }
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        // Check the presence of configured hooks
        if (hooks == null) {
            getLog().info("No configuration is present. Skipping the execution of " + PLUGIN_NAME);
        }

        // TODO: temporary
        if (!alwaysOverride) {
            throw new IllegalStateException("`alwaysOverride = false` is not supported yet. " +
                    "Please, set this parameter to true");
        }

        checkProvidedHooksCorrectness();
        checkGitHooksDirAndCreateIfMissing();

        // Check the hook file
        // Create new or error
        // Check the changes
        // Add the content or do nothing
        for (Map.Entry<String, String> hook : hooks.entrySet()) {
            createHook(hook.getKey(), hook.getValue());
        }
    }

    private void checkProvidedHooksCorrectness() {
        for (Map.Entry<String, String> entry : hooks.entrySet()) {
            if (!GIT_HOOKS.contains(entry.getKey())) {
                throw new IllegalStateException(
                        "`" + entry.getKey() + "` is not a git hook. Available hooks are: " + GIT_HOOKS);
            }
        }
    }

    private void checkGitHooksDirAndCreateIfMissing() throws MojoExecutionException {
        if (!Files.exists(GIT_PATH)) {
            throw new IllegalStateException("It seems that it's not a git repository. " +
                    "Maven goal should be executed from the root of the project.");
        }

        if (!Files.exists(GIT_HOOKS_PATH)) {
            try {
                Files.createDirectories(GIT_HOOKS_PATH);
            } catch (IOException e) {
                throw new MojoExecutionException("Cannot create absent directory " + GIT_HOOKS_PATH, e);
            }
        }
    }

    private void createHook(String hookName, String hookValue) throws MojoExecutionException {
        if (alwaysOverride) {
            String hookPath = GIT_HOOKS_PATH + "/" + hookName;

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(hookPath))) {
                getLog().info("Writing hook `" + hookName + "`");
                writer.write(hookValue.replaceAll("[ ]{2,}", ""));

                Path hookFilePath = Paths.get(hookPath);
                Set<PosixFilePermission> currentPermissions = Files.getPosixFilePermissions(hookFilePath);
                if (!currentPermissions.containsAll(HOOK_FILE_PERMISSIONS)) {
                    Files.setPosixFilePermissions(hookFilePath, HOOK_FILE_PERMISSIONS);
                }

            } catch (IOException e) {
                throw new MojoExecutionException("Cannot write hook `" + hookName + "`", e);
            }
        }
    }
}

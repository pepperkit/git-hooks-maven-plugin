/*
 * Copyright (C) 2021 PepperKit
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package io.github.pepperkit.githooks;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * The main Mojo, installs configured git hooks.
 */
@Mojo(name = "init", defaultPhase = LifecyclePhase.INITIALIZE)
public class InitMojo extends AbstractMojo {

    /**
     * Hooks configured by the user.
     */
    @Parameter(defaultValue = "null")
    public Map<String, String> hooks;

    GitHooksManager gitHooksManager = new GitHooksManager(getLog());

    /**
     * Helper method, is used to set hooks to null if nothing is provided by the user.
     * @param hooks hooks in string format, "null" value is only one expected
     */
    public void setHooks(String hooks) {
        if (hooks.equals("null")) {
            this.hooks = null;
        }
    }

    @Override
    public void execute() throws MojoExecutionException {
        List<File> existingHookFiles = gitHooksManager.getExistingHookFiles();
        if (hooks == null) {
            gitHooksManager.backupExistingHooks(existingHookFiles);
            existingHookFiles.forEach(File::delete);
            return;
        }

        if (gitHooksManager.isFirstLaunchOfPlugin() && !existingHookFiles.isEmpty()) {
            throw new MojoExecutionException("There are existing hooks detected, which were not created using this"
                    + " plugin. Please, make sure that you transferred them to plugin's configuration "
                    + "and delete them manually.");
        }

        gitHooksManager.checkProvidedHookNamesCorrectness(hooks);
        gitHooksManager.checkGitHooksDirAndCreateIfMissing();

        List<File> hookFiles = gitHooksManager.getExistingHookFiles();
        gitHooksManager.backupExistingHooks(hookFiles);

        String hookToBeCreated = null;
        try {
            for (Map.Entry<String, String> hook : hooks.entrySet()) {
                hookToBeCreated = hook.getKey();
                gitHooksManager.createHook(hookToBeCreated, hook.getValue());
            }

        } catch (IOException e) {
            throw new MojoExecutionException("Cannot write hook `" + hookToBeCreated + "`", e);
        }
    }
}

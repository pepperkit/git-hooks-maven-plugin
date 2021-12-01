/*
 * Copyright (C) 2021 PepperKit
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package io.github.pepperkit.githooks;

import java.io.IOException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "validate")
public class ValidateMojo extends AbstractMojo {

    /**
     * The name of the hook to be validated. If not provided, all the hooks will be validated.
     */
    @Parameter(property = "hookName")
    public String hookName;

    GitHooksManager gitHooksManager = new GitHooksManager(getLog());

    @Override
    public void execute() throws MojoExecutionException {
        String hookNameIsValidated = null;
        try {
            if (hookName == null || hookName.isEmpty()) {
                getLog().info("hookName is not provided, validating all the hooks");
                for (String hook : GitHooksManager.GIT_HOOKS) {
                    hookNameIsValidated = hook;
                    gitHooksManager.printHook(hook);
                }
            } else {
                hookNameIsValidated = hookName;
                gitHooksManager.printHook(hookName);
            }
        } catch (IOException e) {
            throw new MojoExecutionException("Cannot read hook file for `" + hookNameIsValidated + "` hook", e);
        }
    }
}

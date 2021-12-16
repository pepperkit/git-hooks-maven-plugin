/*
 * Copyright (C) 2021 PepperKit
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package io.github.pepperkit.githooks;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Prints all or specific hooks installed at the moment, to make sure that the plugin was configured correctly.
 */
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
        GitHooksActionProcessor.processHooks(gitHooksManager::printHook, hookName, getLog());
    }
}

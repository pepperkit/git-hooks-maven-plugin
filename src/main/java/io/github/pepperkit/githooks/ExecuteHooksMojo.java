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
 * Executes all or specific hooks installed at the moment, to make sure that the hooks work as expected,
 * without the need to actually trigger the hook with git action.
 */
@Mojo(name = "executeHooks")
public class ExecuteHooksMojo extends AbstractMojo {

    /**
     * The name of the hook to be tested. If not provided, all the hooks will be tested.
     */
    @Parameter(property = "hookName")
    public String hookName;

    GitHooksManager gitHooksManager = new GitHooksManager(this);

    @Override
    public void execute() throws MojoExecutionException {
        GitHooksActionProcessor.processHooks(gitHooksManager::executeHook, hookName, getLog());
    }
}

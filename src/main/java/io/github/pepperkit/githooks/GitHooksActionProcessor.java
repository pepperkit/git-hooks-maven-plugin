/*
 * Copyright (C) 2021 PepperKit
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package io.github.pepperkit.githooks;

import java.io.IOException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

/**
 * Processes specified {@link GitHooksAction}.
 */
public interface GitHooksActionProcessor {

    /**
     * Processes the specified hook with the specified action, or all the installed hooks, if hook is not specified.
     * @param action action to be performed
     * @param hookName the hook to be processed
     * @param logger mojo's logger
     * @throws MojoExecutionException if action cannot be executed
     */
    static void processHooks(GitHooksAction action, String hookName, Log logger) throws MojoExecutionException {
        String hookNameBeingProcessed = null;
        int hooksProcessed = 0;

        try {
            if (hookName == null || hookName.isEmpty()) {
                logger.info("hookName is not provided, processing all the hooks");
                for (String hook : GitHooksManager.GIT_HOOKS) {
                    hookNameBeingProcessed = hook;
                    boolean processed = action.apply(hook);
                    if (processed) {
                        hooksProcessed++;
                    }
                }
                if (hooksProcessed == 0) {
                    logger.info("No hooks are configured. Make sure you have correctly configured plugin "
                            + "and ran init goal first to install the hooks.");
                }
            } else {
                hookNameBeingProcessed = hookName;
                boolean processed = action.apply(hookName);
                if (!processed) {
                    throw new MojoExecutionException("The specified hook `" + hookName + "` is not installed.");
                }
            }
        } catch (IOException e) {
            throw new MojoExecutionException("Cannot read hook file for `" + hookNameBeingProcessed + "` hook", e);

        } catch (InterruptedException e) {
            logger.error("Cannot execute hook `" + hookNameBeingProcessed + "`");
            Thread.currentThread().interrupt();
        }
    }
}

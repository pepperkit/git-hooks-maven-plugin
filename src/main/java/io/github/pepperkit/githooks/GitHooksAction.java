/*
 * Copyright (C) 2021 PepperKit
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package io.github.pepperkit.githooks;

import java.io.IOException;

/**
 * Generalized git hooks manager's action.
 */
@FunctionalInterface
public interface GitHooksAction {

    /**
     * Applies git hooks action.
     * @param hookName name of the hook
     * @return true if hook existed, false - otherwise
     * @throws IOException if an error occurs on reading or writing the hook
     * @throws InterruptedException if the action was interrupted
     */
    boolean apply(String hookName) throws IOException, InterruptedException;
}

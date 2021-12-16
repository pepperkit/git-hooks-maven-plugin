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
    boolean apply(String hookName) throws IOException, InterruptedException;
}

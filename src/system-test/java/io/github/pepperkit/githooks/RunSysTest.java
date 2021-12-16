/*
 * Copyright (C) 2021 PepperKit
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package io.github.pepperkit.githooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.github.pepperkit.githooks.steps.BaseMojoSysTest;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("io/github/pepperkit/githooks")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "io.github.pepperkit.githooks")
public class RunSysTest extends BaseMojoSysTest {

    @Before
    public void beforeEachScenario() {
        container.start();
    }

    @After
    public void afterEachScenario() {
        container.stop();
    }
}

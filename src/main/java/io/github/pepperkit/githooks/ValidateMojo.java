package io.github.pepperkit.githooks;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.IOException;

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
            }
            hookNameIsValidated = hookName;
            gitHooksManager.printHook(hookName);
        } catch (IOException e) {
            throw new MojoExecutionException("Cannot read hook file for `" + hookNameIsValidated + "` hook", e);
        }
    }
}

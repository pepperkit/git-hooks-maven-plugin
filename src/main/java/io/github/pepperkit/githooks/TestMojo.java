package io.github.pepperkit.githooks;

import java.io.IOException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;


@Mojo(name = "test")
public class TestMojo extends AbstractMojo {

    /**
     * The name of the hook to be tested. If not provided, all the hooks will be tested.
     */
    @Parameter(property = "hookName")
    public String hookName;

    GitHooksManager gitHooksManager = new GitHooksManager(getLog());

    @Override
    // todo: only mojos should throw MojoExecution Exceptions !!!
    public void execute() throws MojoExecutionException {
        String hookNameIsExecuted = null;
        try {
            if (hookName == null || hookName.isEmpty()) {
                getLog().info("hookName is not provided, testing all the hooks");
                for (String hook : GitHooksManager.GIT_HOOKS) {
                    hookNameIsExecuted = hook;
                    gitHooksManager.executeHook(hook);
                }
            }
            hookNameIsExecuted = hookName;
            gitHooksManager.executeHook(hookName);

        } catch (InterruptedException | IOException e) {
            throw new MojoExecutionException("Cannot execute hook `" + hookNameIsExecuted + "`", e);
        }
    }
}
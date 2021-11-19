package io.github.pepperkit.githooks;

import org.apache.maven.plugins.annotations.Parameter;

public class GitHooks {

    @Parameter(property = "applypatch-msg")
    String applyPatchMsg;

    @Parameter(property = "commit-msg")
    String commitMsg;

    @Parameter(property = "fsmonitor-watchman")
    String fsmonitorWatchman;

    @Parameter(property = "post-update")
    String postUpdate;

    @Parameter(property = "pre-applypatch")
    String preApplyPatch;

    @Parameter(property = "pre-commit")
    String preCommit;

    @Parameter(property = "pre-merge-commit")
    String preMergeCommit;

    @Parameter(property = "pre-push")
    String prePush;

    @Parameter(property = "pre-rebase")
    String preRebase;

    @Parameter(property = "pre-receive")
    String preReceive;

    @Parameter(property = "prepare-commit-msg")
    String prepareCommitMsg;

    @Parameter(property = "push-to-checkout")
    String pushToCheckout;

    @Parameter(property = "update")
    String update;

    @Override
    public String toString() {
        return "GitHooks{" +
                "applyPatchMsg='" + applyPatchMsg + '\'' +
                ", commitMsg='" + commitMsg + '\'' +
                ", fsmonitorWatchman='" + fsmonitorWatchman + '\'' +
                ", postUpdate='" + postUpdate + '\'' +
                ", preApplyPatch='" + preApplyPatch + '\'' +
                ", preCommit='" + preCommit + '\'' +
                ", preMergeCommit='" + preMergeCommit + '\'' +
                ", prePush='" + prePush + '\'' +
                ", preRebase='" + preRebase + '\'' +
                ", preReceive='" + preReceive + '\'' +
                ", prepareCommitMsg='" + prepareCommitMsg + '\'' +
                ", pushToCheckout='" + pushToCheckout + '\'' +
                ", update='" + update + '\'' +
                '}';
    }
}

/*
 * Copyright (C) 2021 PepperKit
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package io.github.pepperkit.githooks;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import net.lingala.zip4j.ZipFile;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;

public class GitHooksManager {

    static final String PLUGIN_NAME = "git-hooks-maven-plugin";

    private static final Path GIT_PATH = Paths.get(".git");

    private static final Path GIT_HOOKS_PATH = Paths.get(".git/hooks");

    static final String ARCHIVES_PATH_STR = GIT_HOOKS_PATH + "/archived";

    static final Set<String> GIT_HOOKS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            "applypatch-msg",
            "commit-msg",
            "fsmonitor-watchman",
            "post-update",
            "pre-applypatch",
            "pre-commit",
            "pre-merge-commit",
            "pre-push",
            "pre-rebase",
            "pre-receive",
            "prepare-commit-msg",
            "push-to-checkout",
            "update"
    )));

    private static final Set<PosixFilePermission> HOOK_FILE_PERMISSIONS = Collections.unmodifiableSet(
            new HashSet<>(Arrays.asList(
                    PosixFilePermission.OWNER_READ,
                    PosixFilePermission.OWNER_WRITE,
                    PosixFilePermission.OWNER_EXECUTE
            )));

    private final Log logger;

    public GitHooksManager() {
        logger = new SystemStreamLog();
    }

    public GitHooksManager(Log logger) {
        this.logger = logger;
    }

    void checkProvidedHookNamesCorrectness(Map<String, String> hooks) {
        for (Map.Entry<String, String> entry : hooks.entrySet()) {
            if (!GIT_HOOKS.contains(entry.getKey())) {
                throw new IllegalStateException(
                        "`" + entry.getKey() + "` is not a git hook. Available hooks are: " + GIT_HOOKS);
            }
        }
    }

    void checkGitHooksDirAndCreateIfMissing() {
        if (!Files.exists(GIT_PATH)) {
            throw new IllegalStateException("It seems that it's not a git repository. " +
                    "Maven goal should be executed from the root of the project.");
        }

        if (!Files.exists(GIT_HOOKS_PATH)) {
            try {
                Files.createDirectories(GIT_HOOKS_PATH);
            } catch (IOException e) {
                throw new IllegalStateException("Cannot create directory " + GIT_HOOKS_PATH, e);
            }
        }
    }

    List<File> getExistingHookFiles() {
        return GIT_HOOKS.stream()
                .map(this::getHookPath)
                .filter(h -> Files.exists(Paths.get(h)))
                .map(File::new)
                .collect(Collectors.toList());
    }

    void backupExistingHooks(List<File> hookFiles) {
        try {
            Files.createDirectories(Paths.get(ARCHIVES_PATH_STR));
            zipFiles(hookFiles, ARCHIVES_PATH_STR + "/hooks_" + Instant.now() + ".zip");
        } catch (IOException e) {
            throw new IllegalStateException("Cannot create backup for existing hooks", e);
        }
    }

    private void zipFiles(List<File> srcFiles, String outputFileName) throws IOException {
        if (srcFiles.isEmpty()) {
            logger.info("No hooks existed, nothing to backup");
            return;
        }

        logger.info("Making backup of the existing hooks. They will available in directory" + ARCHIVES_PATH_STR);
        try (ZipFile backup = new ZipFile(outputFileName)) {
            backup.addFiles(srcFiles);
        }
    }

    void createHook(String hookName, String hookValue, boolean alwaysOverride) throws IOException {
        if (alwaysOverride) {
            String hookPath = getHookPath(hookName);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(hookPath))) {
                logger.info("Writing hook `" + hookName + "`");
                writer.write(hookValue.replaceAll("[ ]{2,}", ""));

                Path hookFilePath = Paths.get(hookPath);
                Set<PosixFilePermission> currentPermissions = Files.getPosixFilePermissions(hookFilePath);
                if (!currentPermissions.containsAll(HOOK_FILE_PERMISSIONS)) {
                    Files.setPosixFilePermissions(hookFilePath, HOOK_FILE_PERMISSIONS);
                }
            }
        }
    }

    private String getHookPath(String hookName) {
        return GIT_HOOKS_PATH + "/" + hookName;
    }

    void printHook(String hookName) throws IOException {
        Optional<String> hookValue = readHook(hookName);
        hookValue.ifPresent(h -> logger.info(
                "`" + hookName + "` -> The following commands will be invoked: \n" + h));
    }

    Optional<String> readHook(String hookName) throws IOException {
        Path hookFilePath = Paths.get(getHookPath(hookName));
        if (!Files.exists(hookFilePath)) {
            return Optional.empty();
        }
        return Optional.of(new String(Files.readAllBytes(hookFilePath)));
    }

    void executeHook(String hookName) throws InterruptedException, IOException {
        Optional<String> hook = readHook(hookName);
        if (hook.isPresent()) {
            logger.info(">>>>> Executing hook `" + hookName + "` <<<<<");
            Process process = Runtime.getRuntime().exec("sh -c " + getHookPath(hookName));
            Executors.newSingleThreadExecutor().submit(() -> new BufferedReader(
                    new InputStreamReader(process.getInputStream())).lines().forEach(logger::info));

            int exitCode;
            exitCode = process.waitFor();
            logger.info("Exit code is " + exitCode);
            logger.info(">>>>> The hook `" + hookName + "` was executed with the "
                    + (exitCode == 0 ? "SUCCESS" : "ERROR") + " result <<<<<");
        }
    }
}

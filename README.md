# Git Hooks Maven Plugin
[![Java CI with Maven](https://github.com/pepperkit/git-hooks-maven-plugin/actions/workflows/maven.yml/badge.svg?branch=master)](https://github.com/pepperkit/git-hooks-maven-plugin/actions/workflows/maven.yml)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=pepperkit_git-hooks-maven-plugin&metric=coverage)](https://sonarcloud.io/dashboard?id=pepperkit_git-hooks-maven-plugin)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=pepperkit_git-hooks-maven-plugin&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=pepperkit_git-hooks-maven-plugin)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=pepperkit_git-hooks-maven-plugin&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=pepperkit_git-hooks-maven-plugin)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=pepperkit_git-hooks-maven-plugin&metric=security_rating)](https://sonarcloud.io/dashboard?id=pepperkit_git-hooks-maven-plugin)

Maven plugin for easy git hooks configuration. Provides three goals to work with git hooks:
1. `init` - installs configured git hooks;
2. `validate` - prints all or specific hooks installed at the moment, to make sure that the plugin was configured correctly;
3. `test` - executes all or specific hooks installed at the moment, to make sure that the hooks work as expected, 
   without the need to actually trigger the hook with git action.

## Usage
Add the plugin into your `pom.xml`, configure the hooks, and optionally set the execution to install the hooks each time
the project is rebuild.

The example with *pre-commit* and *pre-push* hooks configured, will look like it:
```xml
<plugins>
    <plugin>
        <groupId>io.github.pepperkit</groupId>
        <artifactId>git-hooks-maven-plugin</artifactId>
        <version>0.9.0</version>
        <executions>
            <!-- It will automatically trigger `init` goal at each initialize project maven phase. -->
            <execution>
                <goals>
                    <goal>init</goal>
                </goals>
            </execution>
        </executions>
        <configuration>
            <hooks>
                <pre-commit>mvn -B checkstyle:checkstyle</pre-commit>
                <pre-push>mvn -B clean compile &amp;&amp; mvn -B test</pre-push>
            </hooks>
        </configuration>
    </plugin>
</plugins>
```

Then you can execute one of the following goals manually:
1. `mvn io.github.pepperkit:git-hooks-maven-plugin:init` - to manually install configured git hooks;
2. `mvn io.github.pepperkit:git-hooks-maven-plugin:validate` - print all the installed hooks to the console;
3. `mvn -DhookName=<hookName> io.github.pepperkit:git-hooks-maven-plugin:validate` - print only the specified hook;
4. `mvn io.github.pepperkit:git-hooks-maven-plugin:test` - execute all the installed hooks;
5. `mvn -DhookName=<hookName> io.github.pepperkit:git-hooks-maven-plugin:test` - execute only the specified hook.

## Project's structure
```
└── src
    ├── main                # code of the plugin
    ├── test                # unit tests
    └── system-test         # system tests
        └── resources       # ssytem tests scenarios and pre-configured pom files needed for the tests
```

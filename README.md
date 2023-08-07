# Git Hooks Maven Plugin
[![Java CI with Maven](https://github.com/pepperkit/git-hooks-maven-plugin/actions/workflows/maven.yml/badge.svg?branch=master)](https://github.com/pepperkit/git-hooks-maven-plugin/actions/workflows/maven.yml)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=pepperkit_git-hooks-maven-plugin&metric=coverage)](https://sonarcloud.io/dashboard?id=pepperkit_git-hooks-maven-plugin)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=pepperkit_git-hooks-maven-plugin&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=pepperkit_git-hooks-maven-plugin)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=pepperkit_git-hooks-maven-plugin&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=pepperkit_git-hooks-maven-plugin)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=pepperkit_git-hooks-maven-plugin&metric=security_rating)](https://sonarcloud.io/dashboard?id=pepperkit_git-hooks-maven-plugin)

Maven plugin for easy git hooks configuration.

## Usage
Add the plugin into your `pom.xml`, configure the hooks, and set the execution to install the hooks each time
the project is rebuild. Be aware that for hooks to be installed to git, any `mvn` goal should be first executed, 
like `compile` or `test`. It means that after editing git-hooks-maven-plugin configuration in `pom.xml`,
it's necessary to manually run `mvn compile` or any other maven goal, on the initializing step of which git hooks will be installed. 

The example with *pre-commit* and *pre-push* hooks configured, will look like this:
```xml
<plugins>
    <plugin>
        <groupId>io.github.pepperkit</groupId>
        <artifactId>git-hooks-maven-plugin</artifactId>
        <version>1.0.0</version>
        <executions>
            <!-- It will automatically trigger `init` goal at each initialize project maven phase. -->
            <execution>
                <goals>
                    <goal>initHooks</goal>
                </goals>
            </execution>
        </executions>
        <configuration>
            <hooks>
                <pre-commit>mvn -B checkstyle:checkstyle</pre-commit>
                <pre-push>mvn -B clean compile && mvn -B test</pre-push>
            </hooks>
        </configuration>
    </plugin>
</plugins>
```

Hook's content is any command line script, which is considered successful if exit code is equal to `0`, and not otherwise.
If execution of the script is successful, git action will be proceeded, if not - it will be cancelled.

## Project's structure
```
└── src
    ├── main                # code of the plugin
    ├── test                # unit tests
    └── system-test         # system tests
        └── resources       # system tests scenarios and pre-configured pom files needed for the tests
```

More about pepperkit projects could be found on its website: https://pepperkit.github.io/ 
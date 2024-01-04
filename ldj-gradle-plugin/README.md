# Living Documentation Gradle plugin

This plugin adds Living Documentation support to Gradle Java projects.

It automatically generates a `livingdocumentation.json` file for the Java source that's in the project every time the project is compiled.
In addition, the task can be run manually with `./gradlew generateLivingDocumentation` (or `./gradlew gLD` for short), or through IDE integration.

The JSON file follows the [JSON documentation](../docs/JSONDocumentation.md) and [JSON Schema](../analyzer-java/src/main/resources/jsonschema/schema.json).
For more information about writing renderers that consume that file, see the [top-level README](../README.md).

## Building the plugin

Use Maven to build the plugin:

```
mvn package
```

The Gradle plugin JAR is now in `target/ldj-gradle-plugin-1-SNAPSHOT.jar`.

## Using the plugin

Add the plugin to your build script's classpath, for example by adding the following to the top of your `build.gradle` file:

```
buildscript {
    repositories {
        flatDir { dir '/path/to/ISEP-LivingDocumentation/ldj-gradle-plugin/target' }
    }
    dependencies {
        classpath 'com.infosupport.livingdocumentation:ldj-gradle-plugin:1-SNAPSHOT'
    }
}
```

Then, apply the plugin:

```
apply plugin: 'com.infosupport.ldoc'
```

The plugin assumes that the `java` plugin is also applied to the project.

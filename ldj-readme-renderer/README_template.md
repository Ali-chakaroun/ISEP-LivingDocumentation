[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=Ali-chakaroun_ISEP-LivingDocumentation&metric=coverage)](https://sonarcloud.io/summary/new_code?id=Ali-chakaroun_ISEP-LivingDocumentation)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=Ali-chakaroun_ISEP-LivingDocumentation&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=Ali-chakaroun_ISEP-LivingDocumentation)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=Ali-chakaroun_ISEP-LivingDocumentation&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=Ali-chakaroun_ISEP-LivingDocumentation)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=Ali-chakaroun_ISEP-LivingDocumentation&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=Ali-chakaroun_ISEP-LivingDocumentation)
# Living Documentation Java

This project is an extension of the [Living Documentation][ldoc] set of tools to
Java. As part of the Living Documentation toolchain, it can be used to generate
documentation and diagrams from source code so that documentation is always
up-to-date.


## State of the project

As of February 2024, this project is linked to a student assignment originating from the University of
Twente and Info Support. The aim of the project is to make the [Living Documentation][ldoc] project
capable to handle multilingual projects. One aspect of this is to make Living Documentation
available for the Java environment; which is the purpose of this repository.

This includes:
- Java Analyzer
- Maven and Gradle plugin to execute the analyzer
- A general support library (the project reader)
- Sample renderers illustrating how Living Documentation can be used


> **Note:** at the moment of writing (February 2024), the student project is coming to an end.
> It is expected that the repository will be moved at some time. When this happens, this repository will refer to the new one.

## Getting started

The process of rendering documentation can be summarized as follows:
1. **Analysis**: Run the Analyzer over the source code of your project. This creates a  Living Documentation JSON file.
    1. Install the analyzer as a maven plugin ([see maven plugin](#maven-plugin))
    2. Include the maven plugin in your project (see  [ldj-maven-plugin-example](/ldj-maven-plugin-example) for an example)
    3. Either rebuild your project in case you added the action to your build pipe, or call the plugin manually (through `mvn ldj:livingdocumentation`)
2. **Render the documentation**: Use a (custom) renderer that reads the generated JSON file and outputs documentation

> Renderers are applications that you can write yourself making use of the support _Libraries_. These interpret the analyzed code and create documentation files.


### Further documentation
[JSON format documentation][json] describing the structure of the JSON file, as well as [a description in JSON Schema format][schema], are available as part of this repository.

Next to these, sample applications along with a renderer are available (see [sample applications renderers](#sample-applications-renderers))


## Analyzer

The analyzer takes Java source code as input and generates a JSON file (containing a representation 
of the AbstractSyntaxTree which is compatible with other Living Documentation applications).

It can be used either as a Java command line application or as a Maven plugin.

### Command line interface
The Java analyzer can be invoked directly as a plain Java application [analyzer-java](/analyzer-java). It accepts the following command
line options:

[~cmd-line-options]

### Maven plugin
The analyzer can also be used as a Maven plugin (which is defined in [ldj-maven-plugin](/ldj-maven-plugin)).
For an example on how to use the Maven plugin, see the `pom.xml` configuration in [ldj-maven-plugin-example](/ldj-maven-plugin-example).

> **Note:** As of right now (February 2024), the Maven plugin is not published to a public Maven repository. 
> If one would like to use the analyzer in the form of a plugin, one can manually install the plugin using Maven install.
> After a manual install, the plugin can be used as defined in the [ldj-maven-plugin-example](/ldj-maven-plugin-example).

### State of analyzer

A list of Java language features which are included in the analyzer can be found in the [JSON format documentation][json].

## Support Libraries

This repository contains the [Project Reader](/ldj-project-reader), which is a support library that eases the creation of renderers. This is done by providing a Java interface to deserialize the Living Documentation JSON file.

## Sample applications renderers
This repository contains two sample applications that make use of the Java Spring framework ([Spring events](/ldj-spring-event-example) & [Spring AMQP](/ldj-spring-amqp-example)).
For both these sample applications a [renderer](/ldj-spring-renderer-example) has been made (which generates a UML sequence diagram). This may serve as a source of inspiration for creating your own renderers.


## Contributing
At the moment, this repository is associated to a student assignment of the University of Twente.
Due to grading criteria, it may not be possible to contribute to this project.

1. Did you find a bug? Please report this, this will help improve the project and does not collide with the educational guidelines.
2. In the case you want to add a new feature, please contact one of the authors. Depending on the feature, it may or may not be possible to implement this yourself.

The university assignment is expected to end around February 2024. From that moment, the repository is expected to be moved. This page will provide an update in case that happens.


[ldoc]: https://github.com/eNeRGy164/LivingDocumentation
[json]: docs/JSONDocumentation.md
[schema]: analyzer-java/src/main/resources/jsonschema/schema.json

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

As of right now, this project is linked to a student assignment originating from the University of
Twente and Info Support. The aim of the project is to make the [Living Documentation][ldoc] project
capable to handle multilingual projects. One aspect of this is to make Living Documentation
available for the Java environment; which is the purpose of this repository.

Within the aspect of making Living Documentation available for the Java environment, two features
can be identified (which are copied from [Living Documentation][ldoc] description):

* [**Analyzer**](#analyzer): A tool to analyze Java projects.
* [**Libraries**](#support-libraries): Assists in generating applications that can create plain text files such as MarkDown, AsciiDoc, PlantUML, Mermaid, and more.

> **Note:** The Libraries are not yet contained within this repository.

## Getting started

The process of rendering documentation can be summarized as follows:
1. **Analysis**: Run the Analyzer over the source code of your project. This creates a  Documentation JSON file.
2. **Render the documentation**: Use a (custom) renderer that reads the generated JSON file and outputs documentation

> Renderers are applications that you can write yourself making use of the support _Libraries_. These interpret the analyzed code and create documentation files.

## Analyzer

The analyzer takes Java source code as input and generates a JSON file (containing a representation 
of the AbstractSyntaxTree which is compatible with other Living Documentation applications).

It can be used either as a Java command line application or as a Maven plugin.

### Command line interface
The Java analyzer can be invoked directly as a plain Java application [analyzer-java](/analyzer-java). It accepts the following command
line options:


| Short option | Long option | Description |
| ------------ | ----------- | ----------- |
|  | `--output` | The file path to save the output JSON to. (Required.) |
|  | `--project` | Root directory of the project to analyze. (Required.) |
| `-p` | `--pretty` | Indent (pretty-print) JSON output.  |



### Maven plugin
The analyzer can also be used as a Maven plugin (which is defined in [ldj-maven-plugin](/ldj-maven-plugin)).
For an example on how to use the Maven plugin, see the `pom.xml` configuration in [ldj-maven-plugin-example](/ldj-maven-plugin-example).

> **Note:** As of right now, the Maven plugin is not published to a public Maven repository. 
> If one would like to use the analyzer in the form of a plugin, one can manually install the plugin using Maven install.
> After a manual install, the plugin can be used as defined in the [ldj-maven-plugin-example](/ldj-maven-plugin-example).

### State of analyzer

A list of Java language features which are included in the analyzer can be found in the JSON schema documentation.
This documentation will be added soon.

## Support Libraries

There is currently no support library available. 

## Contributing
At the moment, this repository is associated to a student assignment of the University of Twente.
Due to grading criteria, it may not be possible to contribute to this project.

1. Did you find a bug? Please report this, this will help improve the project and does not collide with the educational guidelines.
2. In the case you want to add a new feature, please contact one of the authors. Depending on the feature, it may or may not be possible to implement this yourself.

The university assignment is expected to end around February 2024. As of then, these guidelines will be lifted.


[ldoc]: https://github.com/eNeRGy164/LivingDocumentation
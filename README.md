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
Twente and Info Support. The aim of the project is to make the [LivingDocumentation][ldoc] project
capable to handle multilingual projects. One aspect of this is to make LivingDocumentation
available for the Java environment; which is the purpose of this repository.

Within the aspect of making LivingDocumentation available for the Java environment, two features
can be identified (which are copied from [LivingDocumentation][ldoc] description):

* [**Analyzer**](#analyzer): A tool to analyze Java projects.
* [**Libraries**](#support-libraries): Assists in generating applications that can create plain text files such as MarkDown, AsciiDoc, PlantUML, Mermaid, and more.

> **Note**: As this repository is currently association to a University assignment, it is unfortunately not possible to directly contribute to this project. 
> Furthermore, some of the described functionalities may not yet be finalised/are subject to change.

## Getting started

The process of rendering documentation can be summarized as follows:
1. **Analysis**: Run the Analyzer over the source code of your project. This creates a LivingDocumentation JSON file.
2. **Render the documentation**: Use a (custom) renderer that reads the generated JSON file and outputs documentation

> Renderers are applications that you can write yourself making use of the support _Libraries_. These interpret the analyzed code and create documentation files.

## Analyzer

The analyzer takes Java source code as input and generates a JSON file (containing a representation 
of the AbstractSyntaxTree which is compatible with other LivingDocumentation applications).

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

### State of analyzer

The following Java language elements are currently included in the generated JSON:

> [ClassOrInterfaceDeclaration](https://javadoc.io/static/com.github.javaparser/javaparser-core/3.25.6/com/github/javaparser/ast/body/ClassOrInterfaceDeclaration.html), [RecordDeclaration](https://javadoc.io/static/com.github.javaparser/javaparser-core/3.25.6/com/github/javaparser/ast/body/RecordDeclaration.html), [EnumDeclaration](https://javadoc.io/static/com.github.javaparser/javaparser-core/3.25.6/com/github/javaparser/ast/body/EnumDeclaration.html), [EnumConstantDeclaration](https://javadoc.io/static/com.github.javaparser/javaparser-core/3.25.6/com/github/javaparser/ast/body/EnumConstantDeclaration.html), [FieldDeclaration](https://javadoc.io/static/com.github.javaparser/javaparser-core/3.25.6/com/github/javaparser/ast/body/FieldDeclaration.html), [MethodDeclaration](https://javadoc.io/static/com.github.javaparser/javaparser-core/3.25.6/com/github/javaparser/ast/body/MethodDeclaration.html), [ConstructorDeclaration](https://javadoc.io/static/com.github.javaparser/javaparser-core/3.25.6/com/github/javaparser/ast/body/ConstructorDeclaration.html), [Parameter](https://javadoc.io/static/com.github.javaparser/javaparser-core/3.25.6/com/github/javaparser/ast/body/Parameter.html), [MarkerAnnotationExpr](https://javadoc.io/static/com.github.javaparser/javaparser-core/3.25.6/com/github/javaparser/ast/expr/MarkerAnnotationExpr.html), [SingleMemberAnnotationExpr](https://javadoc.io/static/com.github.javaparser/javaparser-core/3.25.6/com/github/javaparser/ast/expr/SingleMemberAnnotationExpr.html), [NormalAnnotationExpr](https://javadoc.io/static/com.github.javaparser/javaparser-core/3.25.6/com/github/javaparser/ast/expr/NormalAnnotationExpr.html), [MemberValuePair](https://javadoc.io/static/com.github.javaparser/javaparser-core/3.25.6/com/github/javaparser/ast/expr/MemberValuePair.html), [ReturnStmt](https://javadoc.io/static/com.github.javaparser/javaparser-core/3.25.6/com/github/javaparser/ast/stmt/ReturnStmt.html), [IfStmt](https://javadoc.io/static/com.github.javaparser/javaparser-core/3.25.6/com/github/javaparser/ast/stmt/IfStmt.html), [ForEachStmt](https://javadoc.io/static/com.github.javaparser/javaparser-core/3.25.6/com/github/javaparser/ast/stmt/ForEachStmt.html), [SwitchStmt](https://javadoc.io/static/com.github.javaparser/javaparser-core/3.25.6/com/github/javaparser/ast/stmt/SwitchStmt.html), [SwitchEntry](https://javadoc.io/static/com.github.javaparser/javaparser-core/3.25.6/com/github/javaparser/ast/stmt/SwitchEntry.html), [AssignExpr](https://javadoc.io/static/com.github.javaparser/javaparser-core/3.25.6/com/github/javaparser/ast/expr/AssignExpr.html), [MethodCallExpr](https://javadoc.io/static/com.github.javaparser/javaparser-core/3.25.6/com/github/javaparser/ast/expr/MethodCallExpr.html), [CatchClause](https://javadoc.io/static/com.github.javaparser/javaparser-core/3.25.6/com/github/javaparser/ast/stmt/CatchClause.html), [JavadocComment](https://javadoc.io/static/com.github.javaparser/javaparser-core/3.25.6/com/github/javaparser/ast/comments/JavadocComment.html)

The links refer to the types as present in the [JavaParser](https://javadoc.io/static/com.github.javaparser/javaparser-core/3.25.6/) library. 
Note that the level of detail is not infinite; some nested constructs may be cut short and included as a string literal.

## Support Libraries

There is currently no support library available. 

[ldoc]: https://github.com/eNeRGy164/LivingDocumentation
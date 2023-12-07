# JSON documentation

This document contains all architectural decisions that have been decided upon that best fit the projects needs. 

# Table skeleton

Below displays how decisions have been organized inside a table for better clarity.

| Name                    | Description                                                                    |
|-------------------------|--------------------------------------------------------------------------------|
| Issue                   | Title relating to what a decision had to be made about                         |
| Date                    | Date on which the decision was made                                            |
| Decision                | The outcome of the decision                                                    |
| Alternatives considered | Other alternatives that were considered, i.e., that which was not the decision |
| Argument (brief)        | Short reasoning behind the made decision                                       |


# Parser

| Name                    | Description                                                                                                                                                                                                                                                                                                                                |
|-------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Issue                   | What Java parser to use                                                                                                                                                                                                                                                                                                                    |
| Date                    | 28/09/2023                                                                                                                                                                                                                                                                                                                                 |
| Decision                | JavaParser                                                                                                                                                                                                                                                                                                                                 |
| Alternatives considered | Tree-sitter                                                                                                                                                                                                                                                                                                                                |
| Argument                | JavaParser was chosen over Tree-sitter for its ability to do type resolving, that is, extracting the qualified names of objects and having built- in methods for traversing the Syntax Tree compared to Tree-sitter where extracting  the qualified names and traversing the Syntax Tree would require manual implementation of the logic. |


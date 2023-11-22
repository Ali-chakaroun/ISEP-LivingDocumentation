
# JSON documentation

This document contains an overview of the possible keys in the intermediary JSON file. It also contains a brief explanation of the meaning of the key in the different programming languages the analyser accepts. Unless indicated otherwise, the terminology used is based on DOTNET.

## Member
This is the top level element of the file.

NET
: A member can represent a class, interface, struct or enum. By default, it is a class (if Type is not specified).

Java
: A member can represent a class, interface, record or enum. By default, it is a class (if Type is not specified).


### Required keys:

| Key      | Type   | .NET                                    | Java                                    |
|----------|--------|-----------------------------------------|-----------------------------------------|
| FullName | string | Name of the class/interface/struct/enum | Name of the class/interface/record/enum |

### Optional keys:

| Key                   | Type                                             | .NET                                                 | Java                                                |
|-----------------------|--------------------------------------------------|------------------------------------------------------|-----------------------------------------------------|
| Type                  | number                                           | 0: class<br/>1: interface<br/>2: struct<br/>3: enum  | 0: class<br/>1: interface<br/>2: record<br/>3: enum |
| Modifiers             | number                                           | See [Modifiers](#modifiers)                          | See [Modifiers](#modifiers)                         |
| Basetypes             | array of strings                                 | Base classes                                         | Super classes                                       |
| Fields                | array of [Field](#field)                         |                                                      |                                                     |
| Constructors          | array of [Constructor](#constructor)             |                                                      |                                                     |
| Methods               | array of [Method](#method)                       |                                                      |                                                     |
| Properties            | array of [Property](#property)                   |                                                      | Not applicable                                      |
| Attributes            | array of [Attribute](#attribute)                 |                                                      | Annotations                                         |
| EnumMembers           | array of [EnumMember](#enummember)               | Enumeration constant                                 | Enumeration constant                                |
| Events                | array of [Event](#event)                         |                                                      | Not applicable                                      |
| DocumentationComments | [DocumentationComments](#documentation-comments) | XML documentation comments                           | JavaDoc                                             |

## Modifiers
Different modifiers (access, non-access, other) are combined into a single value using a bitmap. Currently, modifiers that exist only in Java but not in .NET are not included. These could be added in the future.

The following modifiers are included:

| .NET Modifier | Bitmap Value | Java Equivalent                               |
|---------------|--------------|-----------------------------------------------|
| Internal      | 1 << 0       | None                                          |
| Public        | 1 << 1       | Public                                        |
| Private       | 1 << 2       | Private                                       |
| Protected     | 1 << 3       | Protected                                     |
| Static        | 1 << 4       | Static                                        |
| Abstract      | 1 << 5       | Abstract                                      |
| Override      | 1 << 6       | N/A                                           |
| Readonly      | 1 << 7       | Final (when referring to fields or variables) |
| Async         | 1 << 8       | N/A                                           |
| Const         | 1 << 9       | N/A                                           |
| Sealed        | 1 << 10      | Final (when referring to classes)             |
| Virtual       | 1 << 11      | N/A                                           |
| Extern        | 1 << 12      | N/A                                           |
| New           | 1 << 13      | N/A                                           |
| Unsafe        | 1 << 14      | N/A                                           |
| Partial       | 1 << 15      | N/A                                           |


## Field

### Required keys:

| Key  | Type   | .NET              | Java              |
|------|--------|-------------------|-------------------|
| Name | string | Name of the field | Name of the field |
| Type | string | Type of the field | Type of the field |

### Optional keys:

| Key         | Type               | .NET                           | Java                           |
|-------------|--------------------|--------------------------------|--------------------------------|
| Initializer | string             | The initial value of the field | The initial value of the field |
| Modifiers   | integer, minimum 0 | See [Modifiers](#modifiers)    | See [Modifiers](#modifiers)    |


## Constructor

### Required keys:

| Key  | Type   | .NET                    | Java                    |
|------|--------|-------------------------|-------------------------|
| Name | string | Name of the constructor | Name of the constructor |

### Optional keys:

| Key                    | Type                                              | .NET                                       | Java                         |
|------------------------|---------------------------------------------------|--------------------------------------------|------------------------------|
| Parameters             | array of [Parameter](#parameter)                  |                                            |                              |
| Statements             | array of [Statement](#statement)                  |                                            |                              |
| Modifiers              | integer, minimum 0                                | See [Modifiers](#modifiers)                | See [Modifiers](#modifiers)  |
| Attributes             | array of [Attribute](#attribute)                  |                                            | Annotations                  |
| DocumentationComments  | [DocumentationComments](#documentation-comments)  | XML documentation comments                 | JavaDoc                      |


## Method

### Required keys:

| Key  | Type   | .NET               | Java               |
|------|--------|--------------------|--------------------|
| Name | string | Name of the method | Name of the method |

### Optional keys:

| Key                   | Type                                             | .NET                                       | Java                         |
|-----------------------|--------------------------------------------------|--------------------------------------------|------------------------------|
| ReturnType            | string                                           |                                            |                              |
| Parameters            | array of [Parameter](#parameter)                 |                                            |                              |
| Statements            | array of [Statement](#statement)                 |                                            |                              |
| Modifiers             | integer, minimum 0                               | See [Modifiers](#modifiers)                | See [Modifiers](#modifiers)  |
| Attributes            | array of [Attribute](#attribute)                 |                                            | Annotations                  |
| DocumentationComments | [DocumentationComments](#documentation-comments) | XML documentation comments                 | JavaDoc                      |


## Property

Not applicable for Java.

### Required keys:

| Key  | Type   | .NET                 | Java |
|------|--------|----------------------|------|
| Name | string | Name of the property | N/A  |
| Type | string | Type of the property | N/A  |


### Optional keys:

| Key                   | Type                                             | .NET                              | Java |
|-----------------------|--------------------------------------------------|-----------------------------------|------|
| Initializer           | string                                           | The initial value of the property | N/A  |
| Modifiers             | integer, minimum 0                               | See [Modifiers](#modifiers)       | N/A  |
| Attributes            | array of [Attribute](#attribute)                 |                                   | N/A  |
| DocumentationComments | [DocumentationComments](#documentation-comments) | XML documentation comments        | N/A  |


## Attribute

This is a .NET term, it roughly translates to an annotation in Java.
Note that the arguments in an attribute ([AttributeArgument](#attribute-argument)) hold different properties than [Argument](#argument) as used in other places.

### Required keys:

| Key  | Type   | .NET                  | Java                   |
|------|--------|-----------------------|------------------------|
| Name | string | Name of the attribute | Name of the annotation |
| Type | string | Type of the attribute | Type of the annotation |


### Optional keys:

| Key                   | Type                                              | .NET | Java |
|-----------------------|---------------------------------------------------|------|------|
| Arguments             | array of [AttributeArgument](#attribute-argument) |      |      |

### Attribute Argument

Argument object specifically designed for the arguments passed with an [Attribute](#attribute).

### Required keys:

| Key   | Type   | .NET                  | Java                  |
|-------|--------|-----------------------|-----------------------|
| Name  | string | Name of the argument  | Name of the argument  |
| Type  | string | Type of the argument  | Type of the argument  |
| Value | string | Value of the argument | Value of the argument |


## EnumMember

Member of an enum, meaning a single value in the enum.<br/>
Note that .NET allows assignment of ordinal values to individual enum members, whereas Java does not.<br/> 
Furthermore, note that Java allows passing arguments to enum values, whereas .NET does not.<br/>

### Required keys:

| Key  | Type   | .NET                    | Java                    |
|------|--------|-------------------------|-------------------------|
| Name | string | Name of the enum member | Name of the enum member |


### Optional keys:

| Key                   | Type                                             | .NET                                 | Java                                |
|-----------------------|--------------------------------------------------|--------------------------------------|-------------------------------------|
| Value                 | string                                           | The ordinal value of the enum member | N/A                                 |
| Arguments             | array of [Argument](#argument)                   | N/A                                  | Arguments passed to the enum member |
| Modifiers             | integer, minimum 0                               | See [Modifiers](#modifiers)          | See [Modifiers](#modifiers)         |
| Attributes            | array of [Attribute](#attribute)                 |                                      | Annotations                         |
| DocumentationComments | [DocumentationComments](#documentation-comments) | XML documentation comments           | JavaDoc                             |


## Event

Not applicable for Java.

### Optional keys:

| Key                   | Type                                              | .NET                           | Java |
|-----------------------|---------------------------------------------------|--------------------------------|------|
| Name                  | string                                            | Name of the event              | N/A  |
| Type                  | string                                            | Type of the event              | N/A  |
| Initializer           | string                                            | The initial value of the event | N/A  |
| Modifiers             | integer, minimum 0                                | See [Modifiers](#modifiers)    | N/A  |
| Attributes            | array of [Attribute](#attribute)                  |                                | N/A  |
| DocumentationComments | [DocumentationComments](#documentation-comments)  | XML documentation comments     | N/A  |


## Documentation Comments

Any documentation added to the code.<br/>
For Java this is written as JavaDoc.<br/>
For .NET this is written as XML documentation comments.<br/>
Furthermore, note that JavaDoc allows for a general description, which can contain the information of multiple XML tags.

### Optional keys:

| Key         | Type                  | .NET           | Java                                                   |
|-------------|-----------------------|----------------|--------------------------------------------------------|
| Example     | string                | \<example>     | @example                                               |
| Remarks     | string                | \<remarks>     | N/A, included in description                           |
| Returns     | string                | \<returns>     | @return                                                |
| Summary     | string                | \<summary>     | Description (no tag)                                   |
| Value       | string                | \<value>       | N/A, can be in description                             |
| Exceptions  | object, string values | \<exception>   | @throws                                                |
| Permissions | object, string values | \<permissions> | N/A, usually documented contextually or in description |
| SeeAlsos    | object, string values | \<seealso>     | @see                                                   |
| TypeParams  | object, string values | \<typeparam>   | @param                                                 |


## Parameter

### Required keys:

| Key  | Type   | .NET                  | Java                  |
|------|--------|-----------------------|-----------------------|
| Name | string | Name of the parameter | Name of the parameter |
| Type | string | Type of the parameter | Type of the parameter |


### Optional keys:

| Key             | Type                             | .NET | Java        |
|-----------------|----------------------------------|------|-------------|
| Attributes      | array of [Attribute](#attribute) |      | Annotations |
| HasDefaultValue | boolean                          |      |             |


## Statement

Each statement is one of the following options. The statement type is distinguishable by the key '$type'.

### ForEach

'$type' key should be equal to: "LivingDocumentation.ForEach, LivingDocumentation.Statements".

#### Required keys:

| Key        | Type   | .NET                  | Java                  |
|------------|--------|-----------------------|-----------------------|
| $type      | const  | Type of the statement | Type of the statement |
| Expression | string | Iteration expression  | Iteration expression  |

#### Optional keys:

| Key        | Type                             | .NET | Java |
|------------|----------------------------------|------|------|
| Statements | array of [Statement](#statement) |      |      |

### If
'$type' key should be equal to: "LivingDocumentation.If, LivingDocumentation.Statements".

#### Required keys:

| Key      | Type                                         | .NET                  | Java                  |
|----------|----------------------------------------------|-----------------------|-----------------------|
| $type    | const                                        | Type of the statement | Type of the statement |
| Sections | array of [If Else Section](#if-else-section) | If-else clauses       | If-else clauses       |

#### If Else Section

##### Optional Keys:

| Key        | Type                             | .NET                                                                       | Java                                                                       |
|------------|----------------------------------|----------------------------------------------------------------------------|----------------------------------------------------------------------------|
| Condition  | string                           | 'if' or 'else if' condition, emtpy condition means it is an 'else' clause. | 'if' or 'else if' condition, emtpy condition means it is an 'else' clause. |
| Statements | array of [Statement](#statement) |                                                                            |                                                                            |


### Switch

'$type' key should be equal to: "LivingDocumentation.Switch, LivingDocumentation.Statements".

#### Required keys:

| Key        | Type                                       | .NET                  | Java                  |
|------------|--------------------------------------------|-----------------------|-----------------------|
| $type      | const                                      | Type of the statement | Type of the statement |
| Expression | string                                     | Switch expression     | Switch expression     |

#### Optional keys:

| Key        | Type                                       | .NET                  | Java                  |
|------------|--------------------------------------------|-----------------------|-----------------------|
| Sections   | array of [Switch Section](#switch-section) | Switch case clauses   | Switch case clauses   |


#### Switch Section

##### Optional Keys:

| Key        | Type                             | .NET                                    | Java                                         |
|------------|----------------------------------|-----------------------------------------|----------------------------------------------|
| Labels     | array of string                  | Case labels, empty indicates 'default'. | Case labels, empty indicates 'default' case. |
| Statements | array of [Statement](#statement) |                                         |                                              |


### Invocation

'$type' key should be equal to: "LivingDocumentation.InvocationDescription, LivingDocumentation.Descriptions".

#### Required keys:

| Key   | Type   | .NET                  | Java                  |
|-------|--------|-----------------------|-----------------------|
| $type | const  | Type of the statement | Type of the statement |

#### Optional keys:

| Key             | Type                                              | .NET                                     | Java                                     |
|-----------------|---------------------------------------------------|------------------------------------------|------------------------------------------|
| ContainingType  | string                                            | Class that the invoked method belongs to | Class that the invoked method belongs to |
| Arguments       | array of [AttributeArgument](#attribute-argument) |                                          |                                          |


### Assignment

'$type' key should be equal to: "LivingDocumentation.AssignmentDescription, LivingDocumentation.Descriptions".

#### Required keys:

| Key      | Type   | .NET                              | Java                              |
|----------|--------|-----------------------------------|-----------------------------------|
| $type    | const  | Type of the statement             | Type of the statement             |
| Left     | string | Left-hand side of the assignment  | Left-hand side of the assignment  |
| Operator | string | Assignment operator               | Assignment operator               |
| Right    | string | Right-hand side of the assignment | Right-hand side of the assignment |


### Return

'$type' key should be equal to: "LivingDocumentation.ReturnDescription, LivingDocumentation.Descriptions".

#### Required keys:

| Key        | Type                                       | .NET                  | Java                  |
|------------|--------------------------------------------|-----------------------|-----------------------|
| $type      | const                                      | Type of the statement | Type of the statement |
| Expression | string                                     | Return expression     | Return expression     |


## Argument

### Required keys:

| Key  | Type   | .NET                  | Java                  |
|------|--------|-----------------------|-----------------------|
| Type | string | Type of the argument  | Type of the argument  |
| Text | string | Value of the argument | Value of the argument |

### Optional keys:

| Key  | Type   | .NET                  | Java                  |
|------|--------|-----------------------|-----------------------|
| Type | string | Type of the argument  | Type of the argument  |

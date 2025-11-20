# Mini Spreadsheet (JavaFX + MVVM)

This project is a simplified spreadsheet application inspired by Excel, Google Sheets and OpenCalc.  
It was developed as part of an academic project and implements a functional spreadsheet engine with cell editing, expression parsing, error detection, file saving/loading, and undo/redo capabilities.

The application is built with Java 21, JavaFX 21 and follows the MVVM architecture, using bindings and listeners for UI updates.

---

## Main Features

### Cell Editing and Display
- Each cell contains text content and a computed value.
- Editing is possible via double-click or through the edit bar.
- When not editing, the cell displays the computed value; when editing, it shows the raw content.
- Numbers are formatted automatically:
  - Integers are displayed without decimals.
  - Floating-point numbers are displayed with two decimals.

---

## Expression Engine (Interpreter Pattern)

Expressions are parsed into an abstract syntax tree (AST) using the Interpreter and Builder design patterns.

Supported features:
- Literal values: numbers, booleans, text
- Cell references: `A1`, `D8`, etc.
- Arithmetic operators: `+`, `-`, `*`, `/`
- Logical operators: `and`, `or`, `not`
- Comparison operators: `>`, `>=`, `<`, `<=`, `=`, `!=`
- Automatic recomputation when referenced cells change

Examples:
= 5 + 3 * 2
= B2 + 3 * 5 + C4
= 5 > 3 and 2 > 1 or false
= B1 + C1 > 10

## Error Handling

The spreadsheet detects both syntax errors and evaluation errors.

### Syntax errors  
Displayed as `SYNTAX_ERROR`.  
Examples:
- `=5+3*`
- `=true + 5`
- Invalid references or malformed expressions

### Evaluation errors  
Displayed as `#VALEUR`.  
Examples:
- Type mismatch from referenced cells
- Division by zero (`=5/0`)

### Circular references  
Displayed as `#CIRCULAR_REF` when cells refer to each other directly or indirectly.

---

## SUM Function

A custom `SUM` function is implemented with the syntax:

=SUM(A1:A5)
=SUM(A1:B1)
=SUM(C2:D3)

Rules:
- Supports row ranges, column ranges and rectangular blocks.
- Produces `SYNTAX_ERROR` if the range is invalid.
- Produces `#VALEUR` if any referenced cell contains a non-numeric value.
- Produces `#CIRCULAR_REF` if the target cell is inside the summation range.

---

## File Save and Load (.e4e Format)

The spreadsheet can be saved to and loaded from a custom file format:

File structure:
<ROWS>,<COLUMNS>
<row>,<col>;<content>
...

Example:
10,4
0,0;5
0,2;coucou
1,0;5
1,2;= 5 *2 +7
2,1;=sum(a1:a2)
3,0;=sum(a2:a1)

Features:
- Standard file dialogs (open/save)
- Restores the sheet exactly as saved

---

## Undo / Redo

The application supports:
- Undo (CTRL+Z)
- Redo (CTRL+Y)
- Undo/Redo menu entries, enabled only when applicable
- Multiple undo/redo steps possible

All actions that modify the spreadsheet are tracked.

---

## Architecture (MVVM)

The project follows the MVVM architecture:
- Model: cells, expressions, parsing, AST evaluation
- ViewModel: binds the model to the UI, handles logic
- View: JavaFX UI using SpreadsheetView from ControlsFX

Bindings and listeners ensure:
- Automatic refresh of computed values
- Error propagation
- Undo/Redo state tracking

---

## Known Limitations

(Add your own if needed.)
Examples:
- Parentheses are not implemented unless added as a bonus.
- Unary minus is not supported.
- Only basic numeric formatting is provided.

---

## Technologies

- Java 21  
- JavaFX 21  
- ControlsFX (SpreadsheetView)  
- MVVM architecture  
- Interpreter pattern  
- Builder pattern  

---

## Academic Context

This project was developed as part of the 2024–2025 TGPR course at EPFC.  
Each iteration required:
- an analysis report (PDF)
- updated code
- a tagged release in Git
- a delivery note in the README describing issues and design choices

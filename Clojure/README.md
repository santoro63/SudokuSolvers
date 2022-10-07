# clojuresudoku

An implementation of a sudoku solver.

## Installation

Assumes you have leiningen installed.

1. Clone this repo
2. `cd Clojure` (this directory)
3. `lein uberjar`
4. Copy the generated file wherever you want to save it.


## Usage

Given a file containing a sudoku puzzle, prints the solution on stdout.

    $ java -jar clojuresudoku-0.2.0-SANPSHOT-standalone.jar <puzzlefile>

where `<puzzlefile>` is the path to a file containing a sudoku puzzle.

## Examples

Running against one of the puzzles in the test directory:

```sh
$ java -jar clojuresudoku-0.2.0-SNAPSHOT-standalone.jar test/resources/puzzle1.sudoku
```

Which should print out the following solution:
```
694728531
513964872
782513694
937681425
261495783
458237169
875349216
346172958
129856347
```

# clojuresudoku

An implementation of a sudoku solver.

## Installation

Assumes you have leiningen installed.

1. Clone this repo
2. `cd Clojure` (this directory)
3. `lein jar`
4. Copy the generated file wherever you want to save it.


## Usage

Given a file containing a sudoku puzzle, prints the solution on stdout.

    $ java -jar clojuresudoku-0.1.0-standalone.jar [args]


## Examples

Running against one of the puzzles in the test directory:

```sh
$ java -jar clojouver-0.2.0-SNAPSHOT.jar test/resources/puzzle1.sudoku
```

x

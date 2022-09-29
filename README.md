# SudokuSolvers

Implementations of Sudoku solvers in different languages.
Writing this code is usually my first step in trying to understand a new programming language.

## Algorithm(s)

We use two different algorithms here, based on exhaustive search.
The first version does a breadth-first search, by using a queue. 
It selects the first incomplete puzzle from the queue,
identifies the first empty tile, identifies allowed values for that tile (if fany),
generates new puzzles using those values and adds them to the end of the queue.
All different implementations currently do it that way.

The second version of the algorithm does a depth-first exhaustive search
by using a stack instead of a queue, but the process is pretty much the same.
As I (re)visit these languages I will reimplement the algorithm this way.

package com.puzzles

import com.puzzles.sudoku.Solver
import com.puzzles.sudoku.Solver.Puzzle


/**
 * Solves a sudoku puzzle by exhaustive search, iteratively building solutions and removing
 * unfeasible ones as it finds them. In other words, it gets a puzzle as an array of values
 * with '_' indicated unassigned tiles. It picks the first '_' and creates 9 new solutions
 * with that tile replaced by 1..9 and then filters out the ones that are not valid. Rinse
 * and repeat until either the puzzle is solved or no solutions can be found.
 */
object PuzzleSolver {


  def main(args: Array[String]): Unit = {
    println("Solving puzzle for " + args(0))
    val puzzle = Solver.loadPuzzle(args(0))
    val solution = Solver.solve(puzzle)
    solution match {
      case Some(sol) => Solver.printSolution(sol)
      case None => println("No solution for puzzle")
    }
  }


}

package com.puzzles.sudoku

import scala.io.Source

object Solver {

  type Puzzle = List[Tile]

  val FILLED_VALUES = Set('1', '2', '3', '4', '5', '6', '7', '8', '9')
  val ALLOWED_VALUES = Set('_') ++ FILLED_VALUES

  def loadPuzzle(filename: String): Puzzle = {
    Source.fromFile(filename)
      .getLines()
      .flatMap(_.toCharArray())
      .zipWithIndex
      .map( (t) => Tile(t._2, t._1))
      .toList
  }

  def solve(puzzle: Puzzle): Option[Puzzle] = walkStack(List(puzzle))


  // Expands the candidate set by replacing the first '_' of each candidate
  // with 1..9, creating 9 new candidate solutions
  private def expand_candidates(candidates: List[Puzzle]) = {
    candidates.flatMap(c => expand_candidate(c))
  }

  // Creates 9 candidate solutions for one candidate by replacing
  // the first '_' with 1..9 and eliminating solutions that are not feasible
  private def expand_candidate(candidate: Puzzle): List[Puzzle] = {
    candidate_values(candidate)
      .map(v => replace_first_underscore(candidate, v)).toList
  }

  private def replace_first_underscore(candidate: Puzzle, valueToReplace: Char): Puzzle = {
    candidate match {
      case List() => List()
      case Tile(idx, '_') :: otherVals => Tile(idx, valueToReplace) :: otherVals
      case x :: otherVals => x :: replace_first_underscore(otherVals, valueToReplace)
    }

  }

  private def firstEmptyTile(puzzle: Puzzle): Tile = {
    puzzle.find(_.empty).get
  }

  private def candidate_values(puzzle: Puzzle) : Set[Char] = {
    val tile: Tile = firstEmptyTile(puzzle)
    val puzzleValues: List[Char] = puzzle.filter(_.isRelated(tile)).map(_.content)
    FILLED_VALUES.diff(puzzleValues.toSet)
   }

  // main part_of the algorithm,
  private def walkStack(candidates: List[Puzzle]): Option[Puzzle] = {
    if (candidates.length == 0) {
      None
    } else if (solved(candidates.head)) {
      Some(candidates.head)
    } else {
      val newCandidates = expand_candidate(candidates.head)
      walkStack(newCandidates ++ candidates.tail)
    }
  }

  def printSolution(sol: Puzzle): Unit = {
    if (sol.length == 0) {
      println()
    } else {
      sol.take(9).foreach((t: Tile) => print(t.content))
      println()
      printSolution(sol.drop(9))
    }


  }

  def solved(puzzle: Puzzle) = puzzle.forall( (! _.empty))

}

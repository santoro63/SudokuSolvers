package com.puzzles.sudoku

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
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



  // Creates 9 candidate solutions for one candidate by replacing
  // the first '_' with 1..9 and eliminating solutions that are not feasible
  def expand_candidate(candidate: Puzzle): List[Puzzle] = {
    val refTile = candidate.find(_.empty).get
    val candidateValues = candidate_values(candidate, refTile)
    candidateValues.map(v => replace_first_underscore(candidate,  refTile, v)).toList
  }

  private def replace_first_underscore(candidate: Puzzle, tile: Tile, valueToReplace: Char): Puzzle = {
    val tmp : ArrayBuffer[Tile] = ArrayBuffer()
    tmp ++= candidate
    tmp(tile.index) = Tile(tile.index, valueToReplace)
    tmp.toList
  }

  private def candidate_values(puzzle: Puzzle, tile: Tile) : Set[Char] = {
    val puzzleValues: List[Char] = puzzle.filter(tile.isRelated(_)).map(_.content)
    FILLED_VALUES.diff(puzzleValues.toSet)
   }

  // main part_of the algorithm,
  private def walkStack(candidates: List[Puzzle]): Option[Puzzle] = {
    if (candidates.isEmpty) {
      return None
    }
    val currentPuzzle = candidates.head
    if (solved(currentPuzzle)) {
      return Some(currentPuzzle)
    }
    val otherCandidates = candidates.tail
    val newCandidates = expand_candidate(currentPuzzle)
    walkStack(newCandidates ++ otherCandidates)
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

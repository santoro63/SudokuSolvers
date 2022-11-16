package com.puzzles.sudoku

import com.puzzles.sudoku.Solver.{ALLOWED_VALUES, Puzzle}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.collection.mutable

class SolverSpec extends AnyFlatSpec with Matchers {

  "A Sudoku Solver" should "load a puzzle correctly" in {
    val puzzle = Solver.loadPuzzle("./puzzles/easy.sudoku")
    puzzle.length shouldBe 81
    puzzle(0) shouldBe Tile(0, '_')
    puzzle.map(_.content).forall(Solver.ALLOWED_VALUES.contains(_)) shouldBe true
  }

  "A Sudoku Solver" should "solve puzzles correctly" in {
    val puzzle = Solver.loadPuzzle("./puzzles/easy.sudoku")
    val solvedPuzzle = Solver.solve(puzzle).get
    solvedPuzzle.length shouldBe 81
    puzzle.map(_.content).forall(Solver.FILLED_VALUES.contains(_)) shouldBe true
    allRowsAreGood(puzzle) shouldBe true
    allColsAreGood(puzzle) shouldBe true
    allGrpsAreGood(puzzle) shouldBe true
  }

  private def groupIsGood( tiles: List[Tile]): Boolean = {
    (tiles.length == 9) && (tiles.map(_.content).toSet == ALLOWED_VALUES)
  }

  private def allRowsAreGood(puzzle: Puzzle): Boolean = {
    puzzle.groupBy(_.row).values.toList.forall(groupIsGood(_))
  }
  private def allColsAreGood(puzzle: Puzzle): Boolean = {
    puzzle.groupBy( _.col).values.toList.forall(groupIsGood(_))
  }
  private def allGrpsAreGood(puzzle: Puzzle): Boolean = {
    puzzle.groupBy(_.grp).values.toList.forall(groupIsGood(_))
  }


}

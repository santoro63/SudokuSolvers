/**
 * Created by z0019f3 on 4/29/15.
 */

import scala.collection.parallel.ParSeq
import scala.io.Source

/**
 * Solves a sudoku puzzle by exhaustive search, iteratively building solutions and removing
 * unfeasible ones as it finds them. In other words, it gets a puzzle as an array of values
 * with '_' indicated unassigned tiles. It picks the first '_' and creates 9 new solutions
 * with that tile replaced by 1..9 and then filters out the ones that are not valid. Rinse
 * and repeat until either the puzzle is solved or no solutions can be found.
 */
object SudokuSolver {

  // a puzzle is an array of chars '1' ... '9' or '_'
  type Puzzle = List[Char]

  val TILES  = List('1','2','3','4','5','6','7','8','9').par


  def load_puzzle(filename:String): Puzzle = {
    val lines = Source.fromFile(filename).getLines()
    lines.flatMap(l => l.toCharArray).toList
  }

  def main(args:Array[String]): Unit = {
    println("Solving puzzle for " + args(0))
    val puzzle = load_puzzle(args(0))
    val solutions = solve( List(puzzle).par )
    solutions.foreach(printSolution _)
  }

  // Expands the candidate set by replacing the first '_' of each candidate
  // with 1..9, creating 9 new candidate solutions
  def expand_candidates(candidates: ParSeq[Puzzle]) = {
    candidates.flatMap( c => expand_candidate(c))
  }

  // Creates 9 candidate solutions for one candidate by replacing
  // the first '_' with 1..9 and eliminating solutions that are not feasible
  def expand_candidate(candidate: Puzzle) = {
    TILES
      .map( v => replace_first_underscore(candidate, v))
      .filter( b => isValid(b)).seq
  }

  def replace_first_underscore( candidate: Puzzle, valueToReplace: Char) : Puzzle = {
    candidate match {
      case List() => List()
      case '_' :: otherVals => valueToReplace :: otherVals
      case x :: otherVals => x :: replace_first_underscore(otherVals, valueToReplace)
    }

  }
  // main part_of the algorithm,
  def solve( candidates: ParSeq[Puzzle]): ParSeq[Puzzle] = {
    if (candidates.length == 0) {
      throw new IllegalStateException("no solution found!")
    } else if ( solved(candidates.head)) {
      candidates
    } else {
      println("--> " + candidates.length + " so far")
      solve( expand_candidates(candidates))
    }
  }

  def printSolution(sol: Puzzle): Unit = {
    if (sol.length == 0) {
      println()
    } else {
      sol.take(9).foreach(print)
      println()
      printSolution(sol.drop(9))
    }

  }
  def partitionByRow(tile: (Int,Char)) : Int = tile._1 / 9

  def partitionByColumn(tile: (Int,Char) ) : Int = tile._1 % 9

  def partitionBySquare(tile: (Int,Char)) : Int = {
    val row = partitionByRow(tile) / 3
    val col = partitionByColumn(tile) / 3
    10*row + col
  }

  def groupByFunction( candidate: Puzzle, f: ((Int,Char)) => Int) : Seq[Seq[Char]] = {
    val indexedCandidate = (0 to 80).zip(candidate)
    val groups = indexedCandidate.groupBy[Int](f).values
    groups.map( v => v.map( x => x._2)).toList
  }

  def groupByColumn(candidate: Puzzle): Seq[Seq[Char]] = {
    groupByFunction(candidate, partitionByColumn)
  }

  def groupByRow(candidate: Puzzle) : Seq[Seq[Char]] = {
    groupByFunction(candidate, partitionByRow)
  }

  def groupBySquare(candidate: Puzzle): Seq[Seq[Char]] = {
    groupByFunction(candidate, partitionBySquare)
  }

  def isValid( candidate: Puzzle) : Boolean = {
    groupsAreValid(groupByRow(candidate)) && groupsAreValid(groupByColumn(candidate)) && groupsAreValid(groupBySquare(candidate))
  }

  def groupsAreValid( groups: Seq[Seq[Char]]) : Boolean = {
    groups.forall( g => groupIsValid(g) )
  }

  def groupIsValid( group: Seq[Char]) : Boolean = {
    val blanksRemoved = group.filterNot( _.equals('_'))
    val distinctVals = blanksRemoved.distinct
    blanksRemoved.length == distinctVals.length
  }

  // Returns true if the puzzle has been solved (that is, no _ are left)
  def solved( puzzle : Puzzle) = { ! puzzle.contains('_') }

}

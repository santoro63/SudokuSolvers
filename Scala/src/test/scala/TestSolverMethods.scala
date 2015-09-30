import org.scalatest._

/**
 * Created by z0019f3 on 5/1/15.
 */
class TestSolver extends FlatSpec with Matchers {

  "A solver" should "know when puzzlers are solved or not" in {
      SudokuSolver.solved(List('1','2','3','_')) should be (false)
      SudokuSolver.solved(List('1','2','3','4')) should be (true)
  }

  "A solver" should "replace first underscores correctly" in {
    val solution = List('1', '2', '3')
    SudokuSolver.replace_first_underscore( List('_', '2', '3'), '1') should be(solution)
    SudokuSolver.replace_first_underscore( List('1','_', '3'), '2') should be(solution)
    SudokuSolver.replace_first_underscore(solution, '4') should be(solution)
  }

  "A solver" should "know when a group is valid" in {
    SudokuSolver.isValid(List('1','2','3','4','5','6','7','8','9')) should be (true)
    SudokuSolver.isValid(List('_', '6', '7', '3', '_', '_', '_', '_', '_')) should be (true)
    SudokuSolver.isValid(List('_', '6', '7', '3', '_', '_', '_', '6', '_')) should be (false)
  }

}
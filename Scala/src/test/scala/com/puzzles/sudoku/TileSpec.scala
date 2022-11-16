package com.puzzles.sudoku

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

/** Tests the tile class ana associated functions */
class TileSpec extends AnyFlatSpec with Matchers {

  "A Tile" should "Know its value" in {
    Tile(0,'3').content shouldBe '3'
    Tile (18, '_').content shouldBe '_'
  }

  "A Tile" should "know its group" in {
    Tile(0, '_').grp shouldBe 0
    Tile(5, '_').grp shouldBe 1
    Tile(28, '_').grp shouldBe 3
  }

  "A Tile" should "know if it's empty" in {
    Tile(0, '3').empty shouldBe false
    Tile(0, '_').empty shouldBe true
  }


  "A Tile" should "know related ones" in {
    val testee = new Tile(0, '3')
    val sameRowTile = Tile(4, '4')
    val sameColTile = Tile(18, '3')
    val sameGrpTile = Tile(11, '_')
    testee.isRelated(sameRowTile) shouldBe true
    testee.isRelated(sameColTile) shouldBe true
    testee.isRelated(sameGrpTile) shouldBe true
    testee.isRelated(Tile(12,'1')) shouldBe false
  }

}

package com.puzzles.sudoku

case class Tile(val index: Int, val content: Char = '_') {

  val row: Int = index / 9
  val col: Int = index % 9
  val grp: Int = 3*(row/3) + (col/3)

  def empty: Boolean = content == '_'

  def isRelated(other: Tile): Boolean =
    (this.row == other.row) || (this.col == other.col) || (this.grp == other.grp)
}


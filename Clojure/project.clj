(defproject clojuresudoku "0.2.0-SNAPSHOT"
  :description "A CLI sudoku puzzle solver."
  :url "http://github.com:/santoro63/SudokuSolvers"
  :dependencies [[org.clojure/clojure "1.10.0"]]
  :main clojuresudoku.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})

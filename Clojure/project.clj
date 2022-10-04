(defproject cljsolver "0.2.0-SNAPSHOT"
  :description "A CLI sudoku puzzle solver."
  :url "http://github.com:/santoro63/SudokuSolvers"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]]
  :main ^:skip-aot clojuresudoku.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})

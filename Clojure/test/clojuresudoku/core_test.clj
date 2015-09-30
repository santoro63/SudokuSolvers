(ns clojuresudoku.core-test
  (:require [clojure.test :refer :all]
            [clojuresudoku.core :refer :all]))

;; some fixtures
(def board (range 0 81))
(def indexed-board (zip rcg board))


;; the tests
(deftest solved-test
  (testing "solved returns true"
    (is (solved? '( "1" "2" "3"))))
  (testing "not solved returns false"
    (is (not (solved? '( "1" "_" "3"))))))

(deftest replace-underscore-test
  (testing "replaces if first"
    (is (= '( "1" "2" "3") (replace-first-underscore "1" '("_" "2" "3")))))
  (testing "replaces if middle"
    (is (= '( "1" "2" "3") (replace-first-underscore "2" '("1" "_" "3")))))
  (testing "no change if nothing to replace"
    (is (= '( "4" "5" "6") (replace-first-underscore "3" '("4" "5" "6"))))))

(deftest position-of-first-underscore-test
  (testing "first entry is _"
    (is (= 0 (pos-first-underscore [ "_" "1" "2" "3" ]))))
  (testing "middle entry is _"
    (is (= 3 (pos-first-underscore [ "0" "1" "2" "_" "4" "5" ]))))
  (testing "missing _"
    (is (= -1 (pos-first-underscore [ "0" "1" "2" ])))))

(deftest position-tiles-test
  (testing "get all tiles in position"
    (is (= #{0 1 2 3 4 5 6 7 8 9 18 27 36 45 54 63 72 10 11 19 20}
           (get-position-tiles 0 board)))))

(deftest grouping-test
  (testing "row-group-test"
    (is (= (list 0 1 2 3 4 5 6 7 8) (row-group 0 indexed-board))))
  (testing "col-group-test"
    (is (= (list 1 10 19 28 37 46 55 64 73) (col-group 1 indexed-board))))
  (testing "square-group-test"
    (is (= (list 27 28 29 36 37 38 45 46 47) (square-group 38 indexed-board)))))

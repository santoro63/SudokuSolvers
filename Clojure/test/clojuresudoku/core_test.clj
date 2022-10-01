(ns clojuresudoku.core-test
  (:require [clojure.test :refer :all]
            [clojuresudoku.core :refer :all]))

;;---------------
;; AUX FUNCTIONS
;;---------------

(defn valid-tile?
  [ tile ]
  "Returns true if tile has only allowed values."
  (and (<= 0 (tile :row) 8)
       (<= 0 (tile :col) 8)
       (<= 0 (tile :grp) 8)
       (<= 0 (tile :idx) 80)
       (contains? ALLOWED_VALUES (tile :val))))

;;--------
;; TESTS
;;--------

(deftest make-tile-test

  (testing "tile creation is successful."
    (is (= {:idx  0 :row 0 :col 0 :grp 0 :val \1} (make-tile 0 \1)))
    (is (= {:idx 31 :row 3 :col 4 :grp 4 :val \_} (make-tile 31 \_)))))


(deftest load-puzzle-test

  (testing "load works with good puzzles"
    (let [ testee (load-puzzle "test/resources/puzzle1.sudoku") ]
      (is (= 81 (count testee)))
      (is (every? valid-tile? testee))
      (is (= {:idx  0 :row 0 :col 0 :grp 0 :val \_} (testee 0))) 
      (is (= {:idx 55 :row 6 :col 1 :grp 6 :val \_} (testee 55)))
      ))
  )


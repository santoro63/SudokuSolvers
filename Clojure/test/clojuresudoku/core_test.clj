(ns clojuresudoku.core-test

  (:require [clojure.test :refer :all]
            [clojuresudoku.core :refer :all]))


;;---------------
;; FIXTURES
;;---------------

(def puzzle1 (load-puzzle "test/resources/puzzle1.sudoku"))

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

(defn valid-tile-seq?
  [tile-seq]
  "True if all tiles in the set have distinct values"
  (let [values (map (fn [x] (x :val)) tile-seq)]
    (= SOLUTION_VALUES (set values))))

(defn valid-tile-grp?
  [tile-seq]
  "Valid if all tiles in the group  have a value and are distinct."
  (and (= 9 (count tile-seq))
       (= SOLUTION_VALUES (set (map (fn [x] (x :val)) tile-seq)))))

(defn get-group
  [type id puzzle]
  "Return the group of tiles that have that particular ID for type (one of :row, :col or :grp)."
  (filterv (fn [x] (= (x type) id)) puzzle))


;;--------
;; TESTS
;;--------

(deftest make-tile-test
  
  (testing "tile creation is successful."
    (is (= {:idx  0 :row 0 :col 0 :grp 0 :val \1} (make-tile 0 \1)))
    (is (= {:idx 31 :row 3 :col 4 :grp 4 :val \_} (make-tile 31 \_)))))


(deftest load-puzzle-test

  (testing "load works with good puzzles"
    (let [testee puzzle1] ; we already loaded the puzzle as a fixture
      (is (= 81 (count testee)))
      (is (every? valid-tile? testee))
      (is (= {:idx  0 :row 0 :col 0 :grp 0 :val \_} (testee 0))) 
      (is (= {:idx 55 :row 6 :col 1 :grp 6 :val \_} (testee 55)))
      ))
  )

(deftest candidates-generation-test

  (testing "generates appropriate list of candidates"
    (is (= #{\5 \6} (candidates-for puzzle1 (puzzle1 0))))))

(deftest solve-puzzle-test
  (testing "actually solves puzzles"
    (let [ testee (solve-puzzle (load-puzzle "test/resources/puzzle1.sudoku")) ]
      (is (solved? testee))
      (is (every? valid-tile? testee))
      (is (->> (range 9)
               (map (fn [x] (get-group :row x testee)))
               (every? valid-tile-grp?)))
      (is (->> (range 9)
               (map (fn [x] (get-group :col x testee)))
               (every? valid-tile-grp?)))
      (is (->> (range 9)
               (map (fn [x] (get-group :grp x testee)))
               (every? valid-tile-grp?)))
      ))
  )
            

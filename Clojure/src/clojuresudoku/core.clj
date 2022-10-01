(ns clojuresudoku.core
  (:require [clojure.set :refer :all])
  (:gen-class)
  )

(def SOLUTION_VALUES #{\1 \2 \3 \4 \5 \6 \7 \8 \9})
(def ALLOWED_VALUES (union SOLUTION_VALUES #{\_}))


(defn make-tile
  [index val]
  "Creates a tile map corresponding to INDEX position and with VAL value."
  (let [row (quot index 9)
        col (mod index 9)]
    {:idx index
     :row row
     :col col
     :grp (+ (* 3 (quot row 3)) (quot col 3))
     :val val }))

(defn load-puzzle
  [filename]
  "Creates a puzzle from the specified file"
  (->>
   (slurp filename)
   (filter (fn [c] (contains? ALLOWED_VALUES c)))
   (map-indexed (fn [i v] (make-tile i v)))
   (vec)
   ))

(defn solve-puzzle
  [puzzle-stack]
  "Solves the puzzle in the stack."
  nil)

(defn puzzle-to-string
  [puzzle]
  "Returns a string representation of the puzzle"
  nil)

(defn puzzle-values
  [ puzzle]
  "Return a vector with the contents of the puzzle"
  nil)

(defn -main [& args]
  (->>
   (load-puzzle (first args))
   (solve-puzzle)
   (puzzle-to-string)
   (print)))

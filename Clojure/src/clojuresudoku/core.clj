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

(defn- tile-values [tile-seq]
  (map (fn [x] (x :val)) tile-seq))

(defn load-puzzle
  [filename]
  "Creates a puzzle from the specified file"
  (->>
   (slurp filename)
   (filter (fn [c] (contains? ALLOWED_VALUES c)))
   (map-indexed (fn [i v] (make-tile i v)))
   (vec)
   ))

(defn solved? [puzzle]
  "True if a puzzle is solved (by which we mean complete)."
  (every? (fn [x] (not (= x \_))) (tile-values puzzle)))

(defn- related? [t1 t2]
  "Returns true if the two tiles are related."
  (or (= (t1 :row) (t2 :row))
      (= (t1 :col) (t2 :col))
      (= (t1 :grp) (t2 :grp))))

(defn related-tiles [puzzle tile]
  (filter (fn [x] (related? x tile)) puzzle))

(defn candidates-for
  [puzzle empty-tile]
  "Return a list of all allowed values for the puzzle at the specified index."
  (difference
   ALLOWED_VALUES
   (set    (tile-values (related-tiles puzzle empty-tile)))))

(defn- first-empty
  [puzzle]
  "Returns first empty tile found in the puzzle"
  (let [tile (first puzzle)]
  (cond (nil? puzzle) nil
        (= \_ (tile :val)) tile
        :else (first-empty (rest puzzle)))))

(defn new-puzzle [puzzle empty-tile c]
  (let [idx (empty-tile :idx)]
     (mapv (fn [x] (if (= idx (x :idx)) (make-tile idx c) x)) puzzle)))

(defn new-puzzles
  [puzzle]
  "Create a list of new puzzles from PUZZLE or an empty list if no new puzzles are possible.
The new puzzles are created by replacing the first empty tile with all viable candidates."
  (let [empty-tile (first-empty puzzle)]
     (mapv (fn [c] (new-puzzle puzzle empty-tile c)) (candidates-for puzzle empty-tile))))

(defn solve-puzzle-depth-first
  [puzzle-stack]
  "Does the actual walk-through and solving."
  (let [current-puzzle (peek puzzle-stack)
        stack          (pop puzzle-stack)]
  (cond (nil? current-puzzle) nil
        (solved? current-puzzle) current-puzzle
        :else (solve-puzzle-depth-first (vec (concat stack (new-puzzles current-puzzle)))))))

(defn solve-puzzle
  [puzzle]
  "Solves the puzzle or returns nil if no solution exists."
  (solve-puzzle-depth-first [puzzle]))

  
(defn puzzle-to-string
  [puzzle]
  "Returns a string representation of the puzzle"
  (->>
   puzzle
   (tile-values)
   (partition 9)
   (map (fn [x] (clojure.string/join "" x)))
   (clojure.string/join "\n")
  ))


(defn -main [& args]
  (->>
   (load-puzzle (first args))
   (solve-puzzle)
   (puzzle-to-string)
   (println)
   ))
  

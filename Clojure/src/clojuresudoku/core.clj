(ns clojuresudoku.core
  (:use  clojure.set)
  (:gen-class))

(def SIZE 9)

(defn row-col-group [ idx ]
  (defn get-row [ x ]
    (int (/ x SIZE)))
  (defn get-col [ x ]
    (mod x SIZE))
  (defn get-group [ x ]
    (let [ x-idx (get-row x)
           y-idx (get-col x) ]
        (+ (* 10 (int (/ x-idx 3))) (int (/ y-idx 3)))))
  [ (get-row idx) (get-col idx) (get-group idx) ])

(def rcg (into [] (map row-col-group (range 0 81))))

(defn zip [ l1 l2 ]
  (map vector l1 l2))

(defn pos-belongs [ rcg idx_puzzle_tile ]
  (let [ r1 (first rcg)
        c1 (second rcg)
        g1 (first (rest (rest rcg)))
        puzzle_idx (first idx_puzzle_tile)
        r2 (first puzzle_idx)
        c2 (second puzzle_idx)
        g2 (first (rest (rest puzzle_idx))) 
        ]
    (or (= r1 r2) (= c1 c2) (= g1 g2))))

(defn pos-first-underscore [ puzzle ]
  (.indexOf puzzle "_"))

(defn replace-first-underscore [ pos new-char puzzle ]
  (let [ p (into [] puzzle) ]
        (assoc p pos new-char)))
        
(defn get-position-tiles [pos idx-puzzle]
  (let [ 
        pos-rcg (rcg pos)
        tiles (filter #(pos-belongs pos-rcg %) idx-puzzle) 
        ]
    (into #{} (map #(% 1) tiles))))
          
(defn expand-puzzle [pos puzzle]
  (let [
        idx-puzzle (zip rcg puzzle)
        bad-vals (get-position-tiles pos idx-puzzle)
        candidates (clojure.set/difference #{"1" "2" "3" "4" "5" "6" "7" "8" "9"} bad-vals) ]
    (map #(replace-first-underscore pos % puzzle) candidates)))

(defn expand-puzzles [puzzles]
  (let [ pos (pos-first-underscore (first puzzles)) ]
    (mapcat #(expand-puzzle pos %) puzzles)))

(defn solved? [ puzzle ]
  (not (some #(= "_" %) puzzle)))

(defn solve [ puzzles ]
  (if (solved? (first puzzles))
    puzzles
    (do
      (println "-->" (str (count puzzles)) "so far")
      (recur (expand-puzzles puzzles)))))

(defn print-puzzle [ puzzle ]
  (if (empty? puzzle)
    (println "\n")
    (do
      (println (clojure.string/join (take SIZE puzzle)))
      (print-puzzle (drop SIZE puzzle)))))
    
(defn -main [& args]
  (let [ puzzletext (slurp (first args))
         puzzle (rest (filter #(not (= % "\n")) (clojure.string/split puzzletext #"")))]
    (doseq [ sol (solve (vector puzzle)) ]
      (print-puzzle sol))))

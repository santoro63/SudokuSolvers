(ns clojuresudoku.core
  (:use  clojure.set)
  (:gen-class))

(def SIZE 9)

(defn zip [ l1 l2 ]
  (map vector l1 l2))

(defn grp-func [ [x y] ]
  (+ (* 3 (int (/ x 3))) (int (/ y 3))))


(def row_labels (map #(int (/ % 9)) (range 0 81)))
(def col_labels (map #(mod % 9) (range 0 81)))
(def grp_labels (map grp-func (zip row_labels col_labels)))
                     
(defn get-at-pos [ pos arr ]
  (if (= 0 pos)
    (first arr)
    (get-at-pos (- pos 1) (rest arr))))


(defn pos-first-underscore [ puzzle ]
  (.indexOf puzzle "_"))

(defn replace-first-underscore [ pos new-char puzzle ]
  (let [ p (into [] puzzle) ]
        (assoc p pos new-char)))
        

(defn get-same [ pos labels puzzle ]
  (let [ tile_grp (get-at-pos pos labels) ]
    (map #(second %) (filter #(= tile_grp (first %)) (zip labels puzzle)))))

(defn expand-puzzle [pos puzzle]
  (let [
        row_tiles (get-same pos row_labels puzzle)
        col_tiles (get-same pos col_labels puzzle)
        grp_tiles (get-same pos grp_labels puzzle)
        candidates (clojure.set/difference #{"1" "2" "3" "4" "5" "6" "7" "8" "9"} (into #{} (concat row_tiles col_tiles grp_tiles))) ]
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
        puzzle (filter #(not (= % "\n")) (clojure.string/split puzzletext #""))]
    (doseq [ sol (solve (vector puzzle)) ]
      (print-puzzle sol))))

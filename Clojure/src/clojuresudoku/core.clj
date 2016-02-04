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
        
(defn get-belonging [r rl c cl g gl puzzle_arr]
  (cond (empty? puzzle_arr) '()
        (or (= r (first rl))
            (= c (first cl))
            (= g (first gl))) (cons (first puzzle_arr) (get-belonging r (rest rl) c (rest cl) g (rest gl) (rest puzzle_arr)))
            :else                 (get-belonging r (rest rl) c (rest cl) g (rest gl) (rest puzzle_arr))))

(defn expand-puzzle [pos puzzle]
  (let [
        row (get-at-pos pos row_labels)
        col (get-at-pos pos col_labels)
        grp (get-at-pos pos grp_labels)
        bad_tiles (get-belonging row row_labels col col_labels grp grp_labels puzzle)
        candidates (clojure.set/difference #{"1" "2" "3" "4" "5" "6" "7" "8" "9"} (into #{} bad_tiles)) ]
      (map #(replace-first-underscore pos % puzzle) candidates)))

(defn expand-puzzles [puzzles]
  (let [ pos (pos-first-underscore (first puzzles)) ]
    (mapcat #(expand-puzzle pos %) puzzles)))

(defn solved? [ puzzle ]
  (not (some #(= "_" %) puzzle)))

(defn solve [ puzzles ]
  (if (solved? (first puzzles))
    puzzles
    ;;  (do
    ;;  (println "-->" (str (count puzzles)) "so far")
    (recur (expand-puzzles puzzles))))
    ;;  )

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

(ns aoc.2017.day13
  (:require
    [blancas.kern.core :refer :all]
    [blancas.kern.lexer.basic :refer :all]
    [clojure.tools.trace :refer [trace]]))

(def layer-parser
  (bind [depth dec-lit
         _ colon
         rnge dec-lit]
        (return [depth rnge])))

(defn read-layers
  [lines]
  (->> lines
       (map #(value layer-parser %))
       (into {})))

;Rng 3 (mod 6)
;0 -> 0 
;1 -> 1
;2 -> 2
;3 -> 2
;4 -> 1
;5 -> 0
;
;Rng 2 (mod 4)
;0 -> 0
;1 -> 1
;2 -> 1
;3 -> 0

;Rng 4 (mod 6)
;0  -> 0
;1  -> 1
;2  -> 2
;3  -> 3
;4  -> 2 (4) (6 - 4)
;5  -> 1 (5) (6 - 5)
;6  -> 0 (0)
;7  -> 1 (1)
;8  -> 2 (2)
;9  -> 3 (3)
;10 -> 2 (4) (6 - 4)
;11 -> 1 (5) (6 - 5)
;12 -> 0

;Rng 5 (mod 8)
;5  -> 3 (5) (8 - 5)
;6  -> 2 (6) (8 - 6)
;7  -> 1 (7) (8 - 7)

(defn pico->pos
  [ps rnge]
  (let [r (- (* rnge 2) 2)
        p (mod ps r)]
    (if (>= p rnge) (- r p) p)))

(defn severity
  [m k]
  (* k (get m k 0)))

(defn sum-severity
  [lys total ps]
  (let [rnge (get lys ps)]
    (cond
      (nil? rnge) total
      (zero? (pico->pos ps rnge)) (+ total (* ps rnge))
      :else       total)))

(defn trip-severity
  [lines]
  (let [lys (read-layers lines)
        picosecs (apply max (keys lys))]
    (reduce (partial sum-severity lys) 
            0
            (range (inc picosecs)))))




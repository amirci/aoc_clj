(ns aoc.2021.day5
  (:require [blancas.kern.core :as kc :refer [<*> <$>]]
            [blancas.kern.lexer.basic :as kb]))


(def coord-parser
  (kb/comma-sep kc/dec-num))


(def arrow-parser (kc/token* "-> "))


(def segment-parser
  (<$>
   (juxt first last)
   (<*> coord-parser arrow-parser coord-parser)))


(defn square [x] (* x x))

(defn sign [x]
  (cond
    (pos? x) 1
    (neg? x) -1
    :else 0))


(defn distance
  [[x1 y1] [x2 y2]]
  (let [dx (- x2 x1) dy (- y2 y1)
        sx (sign dx)
        sy (sign dy)
        absx (Math/abs dx)
        absy (Math/abs dy)]
    (cond
      (zero? dx) [absy 0 sy]
      (zero? dy) [absx sx 0]
      :else [absx sx sy])))


(defn extract-points
  [[src tgt]]
  (let [[r dx dy] (distance src tgt)]
    (for [i (range (inc r)) :let [x (* i dx) y (* i dy)]]
      (map + src [x y]))))


(defn diagonal?
  [[[a b] [c d]]]
  (and (not= a c) (not= b d)))


(defn parse-lines
  [lines]
  (map (partial kc/value segment-parser) lines))


(defn calc-lines-overlap
  [lines]
  (->> lines
       (mapcat extract-points)
       frequencies
       (filter (comp (partial <= 2) last))
       count))


(def two-lines-overlap-nd
  (comp calc-lines-overlap
     (partial remove diagonal?)
     parse-lines))


(def two-lines-overlap
  (comp calc-lines-overlap parse-lines))

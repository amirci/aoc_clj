(ns aoc.2016.day8
  (:require [clojure.tools.trace :refer [trace]]
            [clojure.string :refer [split]]))

(defn empty-screen [cols rows] (vec (repeat rows (vec (repeat cols \.)))))

(defn transpose [m] (apply mapv vector m))

(defn to-int [s] (Integer. (str s)))

(def pixel (constantly \#))

(defn shift
  [l n]
  (let [n (- (count l) n)]
    (vec (concat (drop n l) (take n l)))))

(defn shift-row
  [screen row n]
  (update-in screen [row] shift n))

(defmulti rotate (comp keyword first))

(defmethod rotate :column
  [[_ x n screen]]
  (-> screen transpose (shift-row x n) transpose))

(defmethod rotate :row
  [[_ y n screen]]
  (-> screen (shift-row y n)))

(defmulti parse (comp keyword first))

(defmethod parse :rect
  [[_ size screen]]
  (let [[row col] (map to-int (split size #"x"))]
    (->> (for [x (range col) y (range row)] [x y])
         (reduce (fn [s p] (update-in s p pixel)) screen))))

(defmethod parse :rotate
  [[_ kind v _ n screen]]
  (let [v (to-int (last (split v #"=")))
        n (to-int n)]
    (rotate [kind v n screen])))

(defn load-instructions
  [screen instructions]
  (->> instructions
       (map #(split % #" "))
       (reduce (fn [s i] (parse (conj i s))) screen)))

(defn count-pixels
  [instructions]
  (let [screen (empty-screen 50 6)]
    (->> instructions
         (load-instructions screen)
         (apply concat)
         (filter #(= % \#))
         count)))

(defn print-screen
  [instructions]
  (let [screen (empty-screen 50 6)]
    (->> instructions
         (load-instructions screen)
         (map println)
         doall)))

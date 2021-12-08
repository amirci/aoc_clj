(ns aoc.2021.day8
  (:require [clojure.set :as st]))


(defn load-wires
  [lines]
  (->> lines
       (map #(re-seq #"[a-g]+" %))
       (map (partial map sort))
       (map #(split-at 10 %))))


(defn count-digits
  [lines]
  (->> lines
       load-wires
       (mapcat last)
       (filter #(#{2 4 3 7} (count %)))
       count))



(defn find-1-4-7-8
  [{[one] 2 [seven] 3 [four] 4 [eight] 7} m]
  (assoc m
         1 one
         4 four
         7 seven
         8 eight))

(defn find-3
  [{fives 5} {one 1 :as m}]
  (assoc m
         3
         (first (filter #(st/subset? (set one) (set %)) fives))))

(defn overlap
  [nbr coll]
  (reduce
   (fn [m e]
     (assoc m
            (count (st/intersection (set e) (set nbr)))
            e))
   {}
   coll))

(defn find-5-2-6-0-9
  [{fives 5 sixs 6} {seven 7 three 3 four 4 :as m}]
  (let [fives (disj (set fives) three)
        {two 2 five 3} (overlap four fives)
        {six 2} (overlap seven sixs)
        {zero 3 nine 4} (overlap four (disj (set sixs) six))]
    (assoc m
           2 two
           5 five
           0 zero
           6 six
           9 nine)))


(defn ->nbr
  [nbrs]
  (reduce
   (fn [acc n] (+ (* acc 10) n))
   0
   nbrs))

(defn match-wires
  [[wires nbrs]]
  (let [grouped (group-by count wires)]
    (->> [find-1-4-7-8 find-3 find-5-2-6-0-9]
         (reduce (fn [m f] (f grouped m)) {})
         st/map-invert
         (#(map % nbrs))
         ->nbr)))


(defn add-wires
  [lines]
  (->> lines
       load-wires
       (map match-wires)
       (apply +)))

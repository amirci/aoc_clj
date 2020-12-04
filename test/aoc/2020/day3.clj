(ns aoc.2020.day3
  (:require  [clojure.test :as t]))


(defn next-point [len slope p]
  (let [[x y] (map + slope p)]
    [(mod x len) y]))

(def tree? (partial = \#))

(defn slope-down [area slope]
  (let [len (count (first area))]
    (->> [0 0]
         (iterate (partial next-point len slope))
         (take-while #(< (second %) (count area) ))
         (drop 1)
         (map reverse)
         (map (partial get-in area))
         (filter tree?)
         count)))

(defn multiple-slopes [area slopes]
  (->> slopes
     (map (partial slope-down area))
     (apply *)))



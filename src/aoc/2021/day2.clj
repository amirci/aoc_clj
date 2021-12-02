(ns aoc.2021.day2
  (:require [blancas.kern.core :as kc :refer [<*> <$>]]))


(def ops
  {"forward" (fn [n pos] (update pos 0 + n))
   "up" (fn [n pos] (update pos 1 - n))
   "down" (fn [n pos] (update pos 1 + n))})


(defn direction [ops]
  (<$> ops (kc/token* "forward" "up" "down")))

(defn instruction [ops]
  (<$>
   (fn [[f _ n]] (partial f n))
   (<*> (direction ops) kc/white-space kc/dec-num)))

(def instruction1 (instruction ops))

(defn submarine-navigate
  [inst]
  (->> inst
       (map #(kc/value instruction1 %))
       (reduce #(%2 %1) [0 0])
       (apply *)))


(def ops2
  {"forward" (fn [n [x depth aim]]
               [(+ x n) (+ depth (* n aim)) aim])
   "up" (fn [n pos] (update pos 2 - n))
   "down" (fn [n pos] (update pos 2 + n))})


(def instruction2 (instruction ops2))

(defn submarine-navigate2
  [inst]
  (->> inst
       (map #(kc/value instruction2 %))
       (reduce #(%2 %1) [0 0 0])
       (take 2)
       (apply *)))







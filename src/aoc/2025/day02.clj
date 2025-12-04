(ns aoc.2025.day02 
  (:require [clojure.string :as s]
            [clojure.math :as math]
            [clojure.set :as set]))


(defn- invalid-id? [nbr]
  (let [len (inc (long (Math/log10 nbr)))
        half (quot len 2)
        ten-p (int (math/pow 10 half))]
    (and (zero? (mod len 2))
         (= (quot nbr ten-p) (mod nbr ten-p)))))


(defn- find-invalid-ids [range]
  (filter invalid-id? range))


(defn sum-invalid-ids [ranges]
  (->> ranges
       (mapcat find-invalid-ids)
       (apply +)))

(defn- number-repeats? [nbr len]
  (let [ten-p (int (math/pow 10 len))]
    (->> [nbr]
         (iterate (fn [[nbr]] [(quot nbr ten-p) (mod nbr ten-p)]))
         (drop 1)
         (take-while (partial not= [0 0]))
         (map second)
         (apply =))))


(defn- invalid-id-2? [nbr]
  (let [len (inc (long (Math/log10 nbr)))
        half (quot len 2)]
    (->> (range half 0 -1)
         (filter #(zero? (mod len %)))
         (some (partial number-repeats? nbr)))))


(defn sum-invalid-ids-2 [ranges]
  (->> ranges
       (mapcat #(filter invalid-id-2? %))
       (apply +)))


(ns aoc.2024.day3
  (:require [clojure.string :as s]))

(def parse-int #(Integer/parseInt %))


(defn parse-numbers-and-multiply [nbrs]
  (->> nbrs
       (map (partial map parse-int))
       (map (partial apply *))
       (apply +)))


(defn add-multiplications [input]
  (->> input
       (re-seq #"mul\((\d+),(\d+)\)")
       (map rest)
       parse-numbers-and-multiply))


(defn- only-enabled [state [match a b]]
  (condp #(s/starts-with? %2 %1) match
    "mul" (cond-> state
            (:enabled state) (update :result conj [a b]))
    "do()" (assoc state :enabled true)
    (assoc state :enabled false)))


(defn add-enabled-multiplications [input]
  (->> input
       (re-seq #"mul\((\d+),(\d+)\)|do\(\)|don't\(\)")
       (reduce only-enabled {:enabled true :result []})
       :result
       parse-numbers-and-multiply))

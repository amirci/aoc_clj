(ns aoc.day15
  (:require [clojure.tools.trace :refer [trace]]))

(defn transpose
  [ingredients]
  (let [matrix (for [[k v] ingredients] (vals v))]
    (apply mapv vector matrix)))

(def hundred (range 0 101))

(def combinations
  (for [a hundred b hundred c hundred d hundred :when (= (+ a b c d) 100)] [a b c d]))

(defn properties
  [ing qty]
  (->>
    (for [property (take 4 ing)]
      (->> property
           (map * qty)
           (apply +)))
    (filter #(> % 0))
    (apply *)))

(defn best-cookie
  [ingredients]
  (let [transposed (transpose ingredients)]
    (->> combinations
         (map #(properties transposed %))
         (apply max))))






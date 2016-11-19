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

(defn cal-500? [[p cal]] (= 500 cal))

(defn cal-properties
  [ing qty]
  (let [calories (first (drop 4 ing))]
    [(properties ing qty)
     (->> calories (map * qty) (apply +))]))

(defn best-light-cookie
  [ingredients]
  (let [transposed (transpose ingredients)]
    (->> combinations
         (map #(cal-properties transposed %))
         (filter cal-500?)
         (map first)
         (apply max))))


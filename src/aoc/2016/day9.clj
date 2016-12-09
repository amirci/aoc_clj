(ns aoc.2016.day9
  (:require [clojure.tools.trace :refer [trace]]
            [clojure.string :refer [split]]))


(defn decode
  [decoded code]
  (if (empty? code)
    decoded
    (let [[match text _ x y] (re-find #"(\w*)(\((\d+)x(\d+)\))?" code)
          [x y] (if x (map #(Integer. %) [x y]) [0 0])
          code (drop (count match) code)
          expand (->> code (take x) (repeat y) (apply concat))
          code (->> code (drop x))]
      (decode (apply str (concat decoded text expand)) (apply str code)))))

(defn decompress
 [code]
 (decode "" code))


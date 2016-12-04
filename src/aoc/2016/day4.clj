(ns aoc.2016.day4
  (:require [clojure.tools.trace :refer [trace]]))


(defn comp-keys
  [[a b] [c d]]
  (if (= 0 (compare a c))
    (compare b d)
    (- (compare a c))))

(defn real-room?
  [[room-name _ checksum]]
  (->> room-name
       frequencies
       vec
       (map reverse)
       (sort-by identity comp-keys)
       (take 5)
       (map last)
       (apply str)
       (= checksum)))

(defn sum-sectors
  [rooms]
  (->> rooms
       (filter real-room?)
       (map #(Integer. (first (drop 1 %))))
       (reduce +)))

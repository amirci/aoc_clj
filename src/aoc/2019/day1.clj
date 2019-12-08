(ns aoc.2019.day1
  (:require
   [clojure.string :as str]
   ))

; divide by three, round down, and subtract 2.

(def floor #(java.lang.Math/floorDiv %1 %2))

(defn calc-fuel
  [m]
  (-> m
      (floor 3)
      (- 2)))

(defn modules-total-fuel
  [modules]
  (->> modules
       (map calc-fuel)
       (apply +)))

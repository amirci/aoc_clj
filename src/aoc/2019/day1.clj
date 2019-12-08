(ns aoc.2019.day1
  (:require
   [clojure.string :as str]
   ))


(def floor #(java.lang.Math/floorDiv %1 %2))

; divide by three, round down, and subtract 2.
(defn calc-fuel
  [m]
  (-> m
      (floor 3)
      (- 2)))

(defn calc-fuel-additional
  [m]
  (->> m
       (iterate calc-fuel)
       (take-while pos?)
       (drop 1)
       (apply +)))

(defn modules-total-fuel
  ([modules] (modules-total-fuel calc-fuel modules))
  ([f modules]
   (->> modules
        (map f)
        (apply +))))

(def modules-total-fuel-additional (partial modules-total-fuel calc-fuel-additional))

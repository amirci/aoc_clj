(ns aoc.2017.day2
  (:require [clojure.math.combinatorics :as combo]))


(defn to-numbers
  [line]
  (-> line
      (clojure.string/split #"\t")
      (as-> ns (map read-string ns))))

(defn dif-max-min
  [ns]
  (- (apply max ns) (apply min ns)))

(defn divides? [a b] (zero? (rem a b)))

(defn div-divisible
  [ns]
  (->> (combo/combinations ns 2)
       (map (comp reverse sort))
       (filter (partial apply divides?))
       first
       (apply /)))

(defn checksum
  [lines]
  (->> lines
       (map to-numbers)
       (map dif-max-min)
       (apply +)))

(defn checksumb
  [lines]
  (->> lines
       (map to-numbers)
       (map div-divisible)
       (apply +)))




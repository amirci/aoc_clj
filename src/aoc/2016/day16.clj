(ns aoc.2016.day16
  (:require [clojure.tools.trace :refer [trace]]
            [clojure.string :refer [join starts-with? split]]))

(def init-state "00101000101111010")

(defn dc-step
  [a]
  (let [b (->> a reverse (map #(if (= % \1) \0 \1)))]
    (concat a [\0] b)))

(defn dragon-curve
  [state length]
  (loop [curve state]
    (let [curve (dc-step curve)]
      (if (>= (count curve) length)
        (take length curve)
        (recur curve)))))

(defn dragon-curve2
  [state length]
  (->> state
       seq
       (iterate dc-step)
       (drop-while #(< (count %) length))
       first
       (take length)))

(defn checksum
  [code]
  (let [check #{[\0 \0] [\1 \1]}]
    (->> code
         (partition 2)
         (map #(if (check %) \1 \0)))))


(defn odd-checksum
  [state len]
  (loop [cs (dragon-curve state len)]
    (let [cs (checksum cs)]
      (if (odd? (count cs))
        (apply str cs)
        (recur cs)))))

(defn odd-checksum2
  [state len]
  (->> (dragon-curve state len)
       (iterate checksum)
       (drop 1)
       (drop-while #(even? (count %)))
       first
       (apply str)))

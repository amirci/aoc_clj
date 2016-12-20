(ns aoc.2016.day20
  (:require [clojure.tools.trace :refer [trace]]
            [digest :refer :all]))

(defn find-gap
  [[l1 h1] [l2 h2]]
  (if (>= (- l2 h1) 2)
    (reduced (inc h1))
    [l2 h2]))

(defn find-gaps
  [[gaps [l1 h1]] [l2 h2]]
  (let [diff (dec (- l2 h1))
        pair [(max h1 l2) (max h1 h2)]]
    (if (>= diff 1)
      [(conj gaps diff) pair]
      [gaps pair])))

(def find-lowest-ip (partial reduce find-gap))

(defn allowed-ips
  [coll]
  (->> coll
       (drop 1)
       (reduce find-gaps [[] (first coll)])
       first
       (reduce +)))

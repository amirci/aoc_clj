(ns aoc.2016.day1
  (:require [clojure.tools.trace :refer [trace]]))

(defn parse
  [instruction]
  (let [n (Integer. (apply str (drop 1 instruction)))]
    [(first instruction) n]))

(defn abs
  [n]
  (if (neg? n) (- n) n))

(defn from-dir
  [dir lor n]
  (let [nn (- n)]
    (case dir
      :north ({\R [[n  0] :east]  \L [[nn 0] :west]} lor)
      :south ({\R [[nn 0] :west]  \L [[n  0] :east]} lor)
      :west  ({\R [[0  n] :north] \L [[0 nn] :south]} lor)
      :east  ({\R [[0 nn] :south] \L [[0  n] :north]} lor))))

(defn distance
  [[dir pt] [lor n]]
  (let [[new-pt new-dir] (from-dir dir lor n)]
    [new-dir (map + pt new-pt)]))

(defn blocks-away
  [instructions]
  (->> instructions
       (map parse)
       (reduce distance [:north [0 0]])
       last
       (map abs)
       (reduce +)))


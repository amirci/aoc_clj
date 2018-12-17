(ns aoc.2018.day17
  (:require 
    [clojure.string :as str]))

(defn fill-clay
  [clay [a n1 b n2 n3 :as qq]]
  (try
    (let [r1 (range n1 (inc n1))
          r2 (range n2 (inc n3))
          [x-range y-range] (if (= a :x) [r1 r2] [r2 r1])]
      (->> (for [x x-range y y-range] [x y])
           set
           (clojure.set/union clay)))
    (catch Exception e
      (println ">>> EXCEPTION" qq)
      clay)))

(def calc-clay (partial reduce fill-clay #{}))

(defn calc-boundaries
  [points]
  (reduce
    (fn [[min-x min-y max-x max-y] [x y]]
      [(min min-x x)
       (min min-y y)
       (max max-x x)
       (max max-y y)])
    [Integer/MAX_VALUE Integer/MAX_VALUE Integer/MIN_VALUE Integer/MIN_VALUE]
    points))


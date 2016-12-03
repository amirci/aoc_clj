(ns aoc.2016.day3
  (:require [clojure.tools.trace :refer [trace]]))


(defn triangle?
  [[a b c]]
  (and (> (+ a b) c)
       (> (+ b c) a)
       (> (+ a c) b)))

(defn possible-count
  [triangles]
  (->> triangles
       (filter triangle?)
       count))

(ns aoc.2016.day6
  (:require [clojure.tools.trace :refer [trace]]))


(defn decode-msg
  [codes]
  (->> codes
       (apply mapv vector)
       (map (comp first last #(sort-by last %) frequencies))
       (apply str)))


(defn decode-msg-b
  [codes]
  (->> codes
       (apply mapv vector)
       (map (comp first first #(sort-by last %) frequencies))
       (apply str)))



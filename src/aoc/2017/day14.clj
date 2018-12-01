(ns aoc.2017.day14
  (:require 
    [aoc.2017.day10 :as d10]
    [clojure.tools.trace :refer [trace]]))


(defn knot-row
  [word row]
  (->> (str word "-" row)
       d10/knot
       (map #(Integer/bitCount (Integer/parseInt (str %) 16)))
       (apply +)))


;(defn count-:w)
(knot-row "wenycdww" 1)


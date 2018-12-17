(ns aoc.2018.day10
  (:require 
    [clojure.string :as str]))

(defn move-pt [[x y a b]] [(+ x a) (+ y b) a b])

(def tick (partial mapv move-pt))





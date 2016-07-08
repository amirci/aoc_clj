(ns aoc.day1
  (:gen-class))

(defn paren [p]
  (if (= p \( ) 1 -1))

(defn visit
  [inst]
  (reduce + 0 (map paren (seq inst))))

(ns aoc.2017.day8-test
  (:require [clojure.test :refer :all]
            [aoc.2017.day8 :refer :all]))

(def input
  (-> "resources/2017/day8.input.txt"
      slurp
      clojure.string/split-lines))

(def sample-input ["b inc 5 if a > 1"
                   "a inc 1 if b < 5"
                   "c dec -10 if a >= 1"
                   "c inc -20 if c == 10"])

(deftest sample-part-a
  (-> sample-input
      max-value
      (= 1)
      is))

(deftest part-a
  (-> input
      max-value
      (= 7296)
      is))

(deftest part-b
  (-> input
      max-stored-value
      (= 8186)
      is))



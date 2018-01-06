(ns aoc.2017.day10-test
  (:require [clojure.test :refer :all]
            [aoc.2017.day10 :refer :all]))


(def input [70 66 255 2 48 0 54 48 80 141 244 254 160 108 1 41])

(deftest part-a
  (is (= 7888 (first-two input))))

(deftest sample-part-b
  (is (= "a2582a3a0e66e6e86e3812dcb672a272" (knot ""))))


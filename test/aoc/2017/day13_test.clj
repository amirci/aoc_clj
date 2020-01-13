(ns aoc.2017.day13-test
  (:require [clojure.test :refer :all]
            [aoc.2017.day13 :refer :all]))

(def input
  (-> "resources/2017/day13.input.txt"
      slurp
      clojure.string/split-lines))

(deftest ^:slow part-a
  (is (= 1876 (trip-severity input))))

(deftest ^:slow part-b
  (is (= 3964778 (safe-trip-delay input))))


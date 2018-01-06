(ns aoc.2017.day11-test
  (:require [clojure.test :refer :all]
            [aoc.2017.day11 :refer :all]))


(def input
  (-> "resources/2017/day11.input.txt"
      slurp
      clojure.string/split-lines
      first
      (clojure.string/split #",")))

(deftest part-a
  (is (= 805 (path-distance input))))

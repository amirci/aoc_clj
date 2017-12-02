(ns aoc.2017.day2-test
  (:require [clojure.test :refer :all]
            [aoc.2017.day2 :refer :all]))


(deftest part-a
  (-> "resources/2017/day2.input.txt"
      slurp
      clojure.string/split-lines
      checksum
      (= 58975)
      is))

(deftest part-b
  (-> "resources/2017/day2.input.txt"
      slurp
      clojure.string/split-lines
      checksumb
      (= 308)
      is))




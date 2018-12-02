(ns aoc.2018.day2-test
  (:require [clojure.test :refer :all]
            [aoc.2018.day2 :as d2]))

(def input (slurp "resources/2018/day2.input.txt"))

(deftest part-a-test
  (is (= 582 (d1/part-a file))))


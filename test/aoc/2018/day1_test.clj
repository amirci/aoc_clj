(ns aoc.2018.day1-test
  (:require [clojure.test :refer :all]
            [aoc.2018.day1 :as d1]))

(def file (slurp "resources/2018/day1.input.txt"))

(deftest part-a-test
  (is (= 582 (d1/part-a file))))

(deftest sample-b-test
  (is (= 10 (d1/part-b "+3\n+3\n+4\n-2\n-4"))))

(deftest part-b-test
  (is (= 488 (d1/part-b file))))


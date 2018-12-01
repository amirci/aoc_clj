(ns aoc.2018.day1-test
  (:require [clojure.test :refer :all]
            [aoc.2018.day1 :refer :all]))

(deftest a-test
  (is (= 0 (visit "(())")))
)

(deftest ends-in-floor-3
  (is (= 3 (visit "(((")))
  (is (= 3 (visit "(()(()(")))
)



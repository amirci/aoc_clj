(ns aoc.2017.day1-test
  (:require [clojure.test :refer :all]
            [aoc.2017.day1 :refer :all]))

(deftest sample-tests
  (is (= 3 (captcha "1122"))))


(deftest part-a
  (-> "resources/2017/day1.input.txt"
      slurp
      clojure.string/split-lines
      first
      captcha
      (= 1097)
      is))


(deftest sample-tests-b
  (is (= 6 (captcha2 "1212"))))


(deftest part-b
  (-> "resources/2017/day1.input.txt"
      slurp
      clojure.string/split-lines
      first
      captcha2
      (= 1188)
      is))


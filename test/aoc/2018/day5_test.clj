(ns aoc.2018.day5-test
  (:require [clojure.test :refer :all]
            [aoc.2018.day5 :as dut]))

(def input
  (->> "resources/2018/day5.input.txt"
       slurp
       clojure.string/split-lines
       first))

(def component "dabAcCaCBAcCcaDA")

(deftest part-a-sample-test
  (is (= 10 (dut/part-a "dabAcCaCBAcCcaDA"))))

(deftest ^:slow part-a-test
  (is (= 11720
         (dut/part-a input))))

(deftest part-b-sample-test
  (is (= 4
         (dut/part-b component))))


(deftest ^:slow part-b-test
  (is (= 4956
         (dut/part-b input))))


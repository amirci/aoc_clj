(ns aoc.2021.day3-test
  (:require [aoc.2021.day3 :as sut]
            [clojure.test :as t :refer [deftest is]]))

(def input
  (->> "resources/2021/day3.txt"
       slurp
       clojure.string/split-lines))


(def sample
  ["00100"
   "11110"
   "10110"
   "10111"
   "10101"
   "01111"
   "00111"
   "11100"
   "10000"
   "11001"
   "00010"
   "01010"])


(deftest part-1
  (is (= 198 (sut/calc-rates sample)))
  (is (= 3882564 (sut/calc-rates input))) )

(deftest part-2
  (is (= 230 (sut/calc-life-support sample)))
  (is (= 3385170 (sut/calc-life-support input))) )


(ns aoc.2023.day1-test
  (:require [aoc.2023.day1 :as sut]
            [clojure.string :as s]
            [clojure.test :refer [deftest is] :as t]))


(def calibration-doc
  (->> "resources/2023/day1.txt"
       slurp
       s/split-lines))

(def sample
  ["1abc2"
   "pqr3stu8vwx"
   "a1b2c3d4e5f"
   "treb7uchet"])

(deftest part-1
  (is (= 142 (sut/calibration-digits sample)))
  (is (= 54388 (sut/calibration-digits calibration-doc))))


(def sample-2
  ["two1nine"
   "eightwothree"
   "abcone2threexyz"
   "xtwone3four"
   "4nineeightseven2"
   "zoneight234"
   "7pqrstsixteen"])


(deftest part-2
  (is (= 281 (sut/calibration sample-2)))
  (is (= 53515 (sut/calibration calibration-doc))))


(ns aoc.2020.day16-test
  (:require [aoc.2020.day16 :as sut]
            [clojure.java.io :as io]
            [clojure.test :as t :refer :all]))

(def input
  (->> "2020/day16.input.txt"
       io/resource
       slurp
       clojure.string/split-lines))

(def sample
  ["class: 1-3 or 5-7"
   "row: 6-11 or 33-44"
   "seat: 13-40 or 45-50"
   ""
   "your ticket:"
   "7,1,14"
   ""
   "nearby tickets:"
   "7,3,47"
   "40,4,50"
   "55,2,20"
   "38,6,12"])


(deftest part-a
  (testing "sample"
    (is (= 71 (sut/find-invalid sample))))

  (testing "input"
    (is (= 30869 (sut/find-invalid input)))))


(def sample-b
  ["class: 0-1 or 4-19"
   "departure crazy: 0-5 or 8-19"
   "seat: 0-13 or 16-19"
   ""
   "your ticket:"
   "11,12,13"
   ""
   "nearby tickets:"
   "3,9,18"
   "15,1,5"
   "5,14,9"])

(deftest part-b
  (testing "sample"
    (is (= {"departure crazy" 11 "class" 12 "seat" 13} (sut/departures sample-b)))
    (is (= 11 (sut/mul-departures sample-b))))

  (testing "input"
    (is (= 4381476149273 (sut/mul-departures input)))))


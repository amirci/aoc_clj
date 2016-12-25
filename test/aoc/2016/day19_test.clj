(ns aoc.2016.day19-test
  (:require [clojure.test :refer :all]
            [aoc.2016.day19 :refer :all]))


(deftest part-a-sample
  (is (= 3 (white-elephant 5))))


(deftest part-a
  (is (= 1834903 (white-elephant nbr-elves))))


(deftest part-b-sample
  (is (= 2 (white-elephant-2 5))))


(deftest part-b
  (is (= 1420280 (white-elephant-2 nbr-elves))))


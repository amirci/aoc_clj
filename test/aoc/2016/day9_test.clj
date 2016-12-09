(ns aoc.2016.day9-test
  (:require [clojure.test :refer :all]
            [aoc.2016.day9 :refer :all]))

(def instructions
  (-> "resources/2016/day9.input.txt"
      slurp
      clojure.string/split-lines
      first))

(deftest part-a-sample1
  (is (= 6 (count (decompress "ADVENT")))))

(deftest part-a-sample2
  (is (= 7 (count (decompress "A(1x5)BC")))))

(deftest part-a-sample3
  (is (= 9 (count (decompress "(3x3)XYZ")))))

(deftest part-a-sample4
  (is (= 11 (count (decompress "A(2x2)BCD(2x2)EFG")))))

; Rank 600
(deftest part-a
  (is (= 97714 (count (decompress instructions)))))


(ns aoc.2016.day21-test
  (:require [clojure.test :refer [is deftest]]
            [clojure.java.io :as io]
            [clojure.string :as s]
            [aoc.2016.day21 :as sut]))


(def instructions
  (->> "2016/day21.input.txt"
      io/resource
      slurp
      s/split-lines))

(deftest sample-2
  (is (= "edcba" (sut/instruction "ebcda" "swap letter d with letter b"))))

(deftest sample-3
  (is (= "abcde" (sut/instruction "edcba" "reverse positions 0 through 4"))))

(deftest sample-4
  (is (= "bcdea" (sut/instruction "abcde" "rotate left 1 step"))))

(deftest sample-5
  (is (= "bdeac" (sut/instruction "bcdea" "move position 1 to position 4"))))

(deftest sample-5-b
  (is (= "abdec" (sut/instruction "bdeac" "move position 3 to position 0"))))

(deftest sample-6
  (is (= "ecabd" (sut/instruction "abdec" "rotate based on position of letter b"))))

(deftest sample-7
  (is (= "decab" (sut/instruction "abdec" "rotate based on position of letter d"))))

(deftest part-a
  (is (= "dbfgaehc" (sut/scramble "abcdefgh" instructions))))

(deftest part-b
  (is (= "aghfcdeb" (sut/unscramble "fbgdceah" instructions))))

(ns aoc.2016.day21-test
  (:require [clojure.test :refer :all]
            [aoc.2016.day21 :refer :all]))


(def instructions
  (->> "2016/day21.input.txt"
      clojure.java.io/resource
      slurp
      clojure.string/split-lines))

(deftest sample-2
  (is (= "edcba" (instruction "ebcda" "swap letter d with letter b"))))

(deftest sample-3
  (is (= "abcde" (instruction "edcba" "reverse positions 0 through 4"))))

(deftest sample-4
  (is (= "bcdea" (instruction "abcde" "rotate left 1 step"))))

(deftest sample-5
  (is (= "bdeac" (instruction "bcdea" "move position 1 to position 4"))))

(deftest sample-5
  (is (= "abdec" (instruction "bdeac" "move position 3 to position 0"))))

(deftest sample-6
  (is (= "ecabd" (instruction "abdec" "rotate based on position of letter b"))))

(deftest sample-7
  (is (= "decab" (instruction "abdec" "rotate based on position of letter d"))))

(deftest part-a
  (is (= "dbfgaehc" (scramble "abcdefgh" instructions))))

(deftest part-b
  (is (= "aghfcdeb" (unscramble "fbgdceah" instructions))))

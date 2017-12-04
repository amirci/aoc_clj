(ns aoc.2017.day4-test
  (:require [clojure.test :refer :all]
            [aoc.2017.day4 :refer :all]))


(deftest part-a
  (-> "resources/2017/day4.input.txt"
      slurp
      clojure.string/split-lines
      count-valid-passphrases
      (= 477)
      is))

(deftest part-b
  (-> "resources/2017/day4.input.txt"
      slurp
      clojure.string/split-lines
      count-valid-passphrases-anagram
      (= 167)
      is))



(ns aoc.2022.day3-test
  (:require [aoc.2022.day3 :as sut]
            [clojure.string :as s]
            [clojure.test :refer [deftest is] :as t]))


(def rucksacks-list
  (->> "resources/2022/day3.txt"
       slurp
       s/split-lines))

(def sample
  ["vJrwpWtwJgWrhcsFMMfFFhFp"
   "jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL"
   "PmmdzqPrVvPwwTWBwg"
   "wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn"
   "ttgJtRGJQctTZtZT"
   "CrZsJsPPZsGzwwsLwLmpwMDw"])

(deftest part-1
  (is (= 157 (sut/sum-priorities sample)))
  (is (= 7795 (sut/sum-priorities rucksacks-list))))


(deftest part-2
  (is (= 70 (sut/sum-badges sample)))
  (is (= 2703 (sut/sum-badges rucksacks-list))))

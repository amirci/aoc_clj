(ns aoc.2021.day5-test
  (:require [aoc.2021.day5 :as sut]
            [clojure.test :refer [deftest is]]
            [clojure.string :as st]))

(def input
  (->> "resources/2021/day5.txt"
       slurp
       st/split-lines))


(def sample
  ["0,9 -> 5,9"
   "8,0 -> 0,8"
   "9,4 -> 3,4"
   "2,2 -> 2,1"
   "7,0 -> 7,4"
   "6,4 -> 2,0"
   "0,9 -> 2,9"
   "3,4 -> 1,4"
   "0,0 -> 8,8"
   "5,5 -> 8,2"])


(deftest part-1
  (is (= 5 (sut/two-lines-overlap-nd sample)))
  (is (= 5197 (sut/two-lines-overlap-nd input))))


(deftest part-2
  (is (= 12 (sut/two-lines-overlap sample)))
  (is (= 18605 (sut/two-lines-overlap input))))

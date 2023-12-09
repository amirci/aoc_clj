(ns aoc.2023.day7-test
  (:require [aoc.2023.day7 :as sut]
            [clojure.string :as s]
            [clojure.test :refer [deftest is] :as t]))

(def game
  (->> "resources/2023/day7.txt"
       slurp
       s/split-lines))

(def sample
  ["32T3K 765"
   "T55J5 684"
   "KK677 28"
   "KTJJT 220"
   "QQQJA 483"])

(def sample-2
  ["2345A 1"
   "Q2KJJ 13"
   "Q2Q2Q 19"
   "T3T3J 17"
   "T3Q33 11"
   "2345J 3"
   "J345A 2"
   "32T3K 5"
   "T55J5 29"
   "KK677 7"
   "KTJJT 34"
   "QQQJA 31"
   "JJJJJ 37"
   "JAAAA 43"
   "AAAAJ 59"
   "AAAAA 61"
   "2AAAA 23"
   "2JJJJ 53"
   "JJJJ2 41"])

(deftest part-1
  (is (= 6440 (sut/winnings sample)))
  (is (= 6592 (sut/winnings sample-2)))
  (is (= 246795406 (sut/winnings game))))


(deftest part-2
  (is (= 5905 (sut/winnings-j-rule sample)))
  (is (= 6839 (sut/winnings-j-rule sample-2)))
  (is (= 249356515 (sut/winnings-j-rule game))))


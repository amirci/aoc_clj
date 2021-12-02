(ns aoc.2021.day2-test
  (:require [aoc.2021.day2 :as sut]
            [clojure.test :as t :refer [deftest is]]))

(def input
  (->> "resources/2021/day2.txt"
       slurp
       clojure.string/split-lines))

(def sample
  ["forward 5"
   "down 5"
   "forward 8"
   "up 3"
   "down 8"
   "forward 2"])

(deftest part-1
  (is (= 150 (sut/submarine-navigate sample)))
  (is (= 2272262 (sut/submarine-navigate input))))

(deftest part-2
  (is (= 900 (sut/submarine-navigate2 sample)))
  (is (= 2134882034 (sut/submarine-navigate2 input))))

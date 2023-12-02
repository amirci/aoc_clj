(ns aoc.2023.day2-test
  (:require [aoc.2023.day2 :as sut]
            [clojure.string :as s]
            [clojure.test :refer [deftest is] :as t]))


(def games
  (->> "resources/2023/day2.txt"
       slurp
       s/split-lines))

(def sample
  ["Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green"
   "Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue"
   "Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red"
   "Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red"
   "Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green"])

(deftest part-1
  (is (= 8 (sut/possible-games [12 13 14] sample)))
  (is (= 2683 (sut/possible-games [12 13 14] games))))


(deftest part-2
  (is (= 2286 (sut/sum-of-powers sample)))
  (is (= 49710 (sut/sum-of-powers games))))

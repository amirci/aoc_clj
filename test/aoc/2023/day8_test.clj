(ns aoc.2023.day8-test
  (:require [aoc.2023.day8 :as sut]
            [clojure.string :as s]
            [clojure.math.numeric-tower :as math]
            [clojure.test :refer [deftest is] :as t]))


(def instructions
  (->> "resources/2023/day8.txt"
       slurp
       s/split-lines))

(def sample
  ["RL"
   ""
   "AAA = (BBB, CCC)"
   "BBB = (DDD, EEE)"
   "CCC = (ZZZ, GGG)"
   "DDD = (DDD, DDD)"
   "EEE = (EEE, EEE)"
   "GGG = (GGG, GGG)"
   "ZZZ = (ZZZ, ZZZ)"])


(deftest part-1
  (is (= 2 (sut/count-steps-to-terminus sample)))
  (is (= 20221 (sut/count-steps-to-terminus instructions))))


(deftest parsing-test
  (is (= {"AAA" ["BBB" "CCC"]} (sut/parse-mappings ["AAA = (BBB, CCC)"]))))

(def sample-2
  ["LR"
   ""
   "11A = (11B, XXX)"
   "11B = (XXX, 11Z)"
   "11Z = (11B, XXX)"
   "22A = (22B, XXX)"
   "22B = (22C, 22C)"
   "22C = (22Z, 22Z)"
   "22Z = (22B, 22B)"
   "XXX = (XXX, XXX)"])


(deftest part-2
  (is (= 6 (sut/count-ghost-steps-to-terminus sample-2)))
  (is (= 14616363770447 (sut/count-ghost-steps-to-terminus instructions))))


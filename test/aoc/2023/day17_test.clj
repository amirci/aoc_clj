(ns aoc.2023.day17-test
  (:require [aoc.2023.day17 :as sut]
            [clojure.string :as s]
            [clojure.test :refer [deftest is] :as t]))

(def input
  (->> "resources/2023/day17.txt"
       slurp
       s/split-lines
       (mapv (partial mapv #(Character/digit % 10)))))

(def sample
  ["2413432311323"
   "3215453535623"
   "3255245654254"
   "3446585845452"
   "4546657867536"
   "1438598798454"
   "4457876987766"
   "3637877979653"
   "4654967986887"
   "4564679986453"
   "1224686865563"
   "2546548887735"
   "4322674655533"])


(def sample-heats
  (->> sample
       (mapv (partial mapv #(Character/digit % 10)))))


(deftest part1-test
  (is (= 102 (sut/path-min-heat-loss sample-heats)))
  (is (= 847 (sut/path-min-heat-loss input))))

(def sample-ultra
  (->> ["111111111111"
        "999999999991"
        "999999999991"
        "999999999991"
        "999999999991"]
       (mapv (partial mapv #(Character/digit % 10)))))


(deftest part2-test
  (is (= 94 (sut/path-min-heat-loss-ultra sample-heats)))
  (is (= 71 (sut/path-min-heat-loss-ultra sample-ultra)))
  (is (= 997 (sut/path-min-heat-loss-ultra input))))


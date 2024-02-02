(ns aoc.2023.day21-test
  (:require [aoc.2023.day21 :as sut]
            [clojure.string :as s]
            [clojure.test :refer [deftest is] :as t]))

(def input
  (->> "resources/2023/day21.txt"
       slurp
       s/split-lines))

(def sample
  ["..........."
   ".....###.#."
   ".###.##..#."
   "..#.#...#.."
   "....#.#...."
   ".##..S####."
   ".##..#...#."
   ".......##.."
   ".##.#.####."
   ".##..##.##."
   "..........."])

(deftest part1-test
  (is (= 1 (sut/reach-after-steps 16 sample))))

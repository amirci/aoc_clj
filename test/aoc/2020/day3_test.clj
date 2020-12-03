(ns aoc.2020.day3-test
  (:require [aoc.2020.day3 :as sut]
            [clojure.java.io :as io]
            [clojure.test :as t :refer :all]))

(def input
  (->> "2020/day3.input.txt"
       io/resource
       slurp
       clojure.string/split-lines))


(def sample-a
  ["..##......."
   "#...#...#.."
   ".#....#..#."
   "..#.#...#.#"
   ".#...##..#."
   "..#.##....."
   ".#.#.#....#"
   ".#........#"
   "#.##...#..."
   "#...##....#"
   ".#..#...#.#"])


(deftest part-a
  (testing "example"
    (is (= 7 (sut/slope-down sample-a [3 1]))))

  (testing "input file"
    (is (= 272 (sut/slope-down input [3 1])))))


(def slopes
  [[1 1]
   [3 1]
   [5 1]
   [7 1]
   [1 2]])

(deftest part-b
  (testing "sample with all the slopes"
    (is (= 336 (sut/multiple-slopes sample-a slopes))))

  (testing "input file with all the slopes"
    (is (= 3898725600 (sut/multiple-slopes input slopes)))))

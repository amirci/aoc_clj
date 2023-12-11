(ns aoc.2023.day10-test
  (:require [aoc.2023.day10 :as sut]
            [clojure.string :as s]
            [clojure.test :refer [deftest is] :as t]
            [clojure.string :as st]))

(def input
  (->> "resources/2023/day10.txt"
       slurp
       s/split-lines))


(def sample-square
  ["....."
   ".S-7."
   ".|.|."
   ".L-J."
   "....."])

(def sample-complex
  ["..F7."
   ".FJ|."
   "SJ.L7"
   "|F--J"
   "LJ..."])


(deftest pipes-test
  (is (= {[2 1] [1 2] [1 2] [2 1]} (sut/f-pipe [1 1])))
  (is (= {[2 0] [1 1] [1 1] [2 0]} (sut/j-pipe [2 1])))
  (is (= {[3 0] [4 1] [4 1] [3 0]} (sut/l-pipe [4 0])))
  (is (= {[0 2] [1 3] [1 3] [0 2]} (sut/seven-pipe [0 3])))
  (is (= {[2 0] [4 0] [4 0] [2 0]} (sut/v-pipe [3 0])))
  (is (= {[3 1] [3 3] [3 3] [3 1]} (sut/h-pipe [3 2]))))


(deftest part-1-test
  (is (= 4 (sut/furthest-point sample-square)))
  (is (= 8 (sut/furthest-point sample-complex)))
  (is (= 6864 (sut/furthest-point input))))


(def sample-loop-1
  ["..........."
   ".S-------7."
   ".|F-----7|."
   ".||OOOOO||."
   ".||OOOOO||."
   ".|L-7OF-J|."
   ".|..|O|..|."
   ".L--JOL--J."
   ".....O....."])


(def sample-loop-2
  [".F----7F7F7F7F-7...."
   ".|F--7||||||||FJ...."
   ".||.FJ||||||||L7...."
   "FJL7L7LJLJ||LJ.L-7.."
   "L--J.L7...LJS7F-7L7."
   "....F-J..F7FJ|L7L7L7"
   "....L7.F7||L7|.L7L7|"
   ".....|FJLJ|FJ|F7|.LJ"
   "....FJL-7.||.||||..."
   "....L---J.LJ.LJLJ..."])

(def sample-loop-3
  ["FF7FSF7F7F7F7F7F---7"
   "L|LJ||||||||||||F--J"
   "FL-7LJLJ||||||LJL-77"
   "F--JF--7||LJLJ7F7FJ-"
   "L---JF-JLJ.||-FJLJJ7"
   "|F|F-JF---7F7-L7L|7|"
   "|FFJF7L7F-JF7|JL---7"
   "7-L-JL7||F7|L7F-7F7|"
   "L.L7LFJ|||||FJL7||LJ"
   "L7JLJL-JLJLJL--JLJ.L"])

(deftest part-2-test
  (is (= 4 (sut/enclosed-count sample-loop-1)))
  (is (= 8 (sut/enclosed-count sample-loop-2)))
  (is (= 10 (sut/enclosed-count sample-loop-3)))
  (is (= 349 (sut/enclosed-count input))))


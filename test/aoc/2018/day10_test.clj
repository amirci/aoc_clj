(ns aoc.2018.day10-test
  (:require [clojure.test :refer :all]
            [aoc.2018.day10 :as dut]))

(def input
  (-> "resources/2018/day10.input.txt"
      slurp
      (clojure.string/replace "position=<" "")
      (clojure.string/replace "> velocity=<" ",")
      (clojure.string/replace ">" "")
      clojure.string/split-lines
      (->>
        (map #(read-string (str "[" % "]"))))))

(reduce
  [Integer/MAX_VALUE Integer/MAX_VALUE Integer/MIN_VALUE Integer/MIN_VALUE]
  input)
(apply max (map first input))
(apply max (map second input))

(def sample-input
  [[ 9  1  0  2]
   [ 7  0 -1  0]
   [ 3 -2 -1  1]
   [ 6 10 -2 -1]
   [ 2 -4  2  2]
   [-6 10  2 -2]
   [ 1  8  1 -1]
   [ 1  7  1  0]
   [-3 11  1 -2]
   [ 7  6 -1 -1]
   [-2  3  1  0]
   [-4  3  2  0]
   [10 -3 -1  1]
   [ 5 11  1 -2]
   [ 4  7  0 -1]
   [ 8 -2  0  1]
   [15  0 -2  0]
   [ 1  6  1  0]
   [ 8  9  0 -1]
   [ 3  3 -1  1]
   [ 0  5  0 -1]
   [-2  2  2  0]
   [ 5 -2  1  2]
   [ 1  4  2  1]
   [-2  7  2 -2]
   [ 3  6 -1 -1]
   [ 5  0  1  0]
   [-6  0  2  0]
   [ 5  9  1 -2]
   [14  7 -2  0]
   [-3  6  2 -1]])




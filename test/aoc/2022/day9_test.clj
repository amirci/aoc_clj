(ns aoc.2022.day9-test
  (:require [aoc.2022.day9 :as sut]
            [clojure.string :as s]
            [clojure.test :refer [deftest is are] :as t]))

(def input
  (->> "resources/2022/day9.txt"
       slurp
       s/split-lines
       ))

(def sample
  (->> ["R 4"
        "U 4"
        "L 3"
        "D 1"
        "R 4"
        "D 1"
        "L 5"
        "R 2"]))

(deftest move-towards-test
  (are [t-pos h-pos expected] (= expected (sut/move-towards t-pos h-pos))
    [0 0] [-2  0] [-1  0]
    [0 0] [ 0 -2] [ 0 -1]
    [0 0] [ 0  2] [ 0  1]
    [0 0] [ 2  0] [ 1  0]

    [0 0] [-2 -2] [-1 -1]
    [0 0] [ 2 -2] [ 1 -1]
    [0 0] [ 2  2] [ 1  1]
    [0 0] [-2  2] [-1  1]

    [0 0] [-2 -1] [-1 -1]
    [0 0] [-2  1] [-1  1]
    [0 0] [-1 -2] [-1 -1]
    [0 0] [-1  2] [-1  1]
    [0 0] [ 1 -2] [ 1 -1]
    [0 0] [ 1  2] [ 1  1]
    [0 0] [ 2 -1] [ 1 -1]
    [0 0] [ 2  1] [ 1  1]
    [-3 4] [-4 2] [-4 3]))


#_(deftest history-test
  (is (= #{[0 0] [0 1] [0 2] [0 3]
           [-1 4]
           [-2 1] [-2 2] [-2 3] [-2 4]
           [-3 3] [-3 4]
           [-4 2] [-4 3]}
         (set (sut/simulate-moves sample)))))
; ..##..
; ...##.
; .####.
; ....#.
; s###..

(deftest part-1
  (is (= 13 (sut/simulate-single-tail sample)))
  (is (= 6494 (sut/simulate-single-tail input))))

(deftest part-2
  (is (= 1 (sut/simulate-multi-tail sample)))
  (is (= 2691 (sut/simulate-multi-tail input))))

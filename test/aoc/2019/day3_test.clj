(ns aoc.2019.day3-test
  (:require
   [aoc.2019.day3 :as sut]
   [clojure.java.io :as io]
   [clojure.edn :as edn]
   [clojure.test :as t :refer [deftest is]]))

(def input (slurp (io/resource "2019/day3.input.txt")))

(def input-paths
  (->> input
       clojure.string/split-lines
       (map #(clojure.string/split % #","))))

(deftest path-points-test
  (is (= [[0 1] [0 2] [0 3] [0 4] [0 5] [0 6] [0 7] [0 8]
          [1 8] [2 8] [3 8] [4 8] [5 8]
          [5 7] [5 6] [5 5] [5 4] [5 3]
          [4 3] [3 3] [2 3]]
         (sut/path-points ["R8" "U5" "L5" "D3"])
         )))

(sut/path-points ["U7" "R6" "D4" "L4"])
(sut/path-points ["R8" "U5" "L5" "D3"])

(deftest intersection-test
  (is (= 6
         (sut/closest-intersection
          ["U7" "R6" "D4" "L4"]
          ["R8" "U5" "L5" "D3"])))
  (is (= 159
         (sut/closest-intersection
          ["R75" "D30" "R83" "U83" "L12" "D49" "R71" "U7" "L72"]
          ["U62" "R66" "U55" "R34" "D71" "R55" "D58" "R83"]
          ))))

(deftest part-a-test
  (is (= 6
         (apply sut/closest-intersection input-paths))))

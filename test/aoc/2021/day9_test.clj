(ns aoc.2021.day9-test
  (:require [aoc.2021.day9 :as sut]
            [clojure.string :as st]
            [clojure.test :as t :refer [deftest is]]))

(def input
  (->> "resources/2021/day9.txt"
       slurp
       st/split-lines
       (map #(vec (map read-string (re-seq #"\d" %))))
       vec))


(def sample
  [[2 1 9 9 9 4 3 2 1 0]
   [3 9 8 7 8 9 4 9 2 1]
   [9 8 5 6 7 8 9 8 9 2]
   [8 7 6 7 8 9 6 7 8 9]
   [9 8 9 9 9 6 5 6 7 8]])



(deftest part-1
  (is (= 15 (sut/sum-low-points sample)))
  (is (= 577 (sut/sum-low-points input))))


(deftest part-2
  (is (= 1134 (sut/find-largest-basins sample)))
  (is (= 1069200 (sut/find-largest-basins input))))

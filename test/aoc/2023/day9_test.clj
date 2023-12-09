(ns aoc.2023.day9-test
  (:require [aoc.2023.day9 :as sut]
            [clojure.string :as s]
            [clojure.test :refer [deftest is] :as t]))


(def histories
  (->> "resources/2023/day9.txt"
       slurp
       s/split-lines
       (map #(read-string (str \[ % \])))))


(def sample
  [[ 0 3 6 9 12 15 ]
   [ 1 3 6 10 15 21 ]
   [ 10 13 16 21 30 45 ]])

(deftest part-1
  (is (= 114 (sut/sum-extrapolation-end sample)))
  (is (= 1887980197 (sut/sum-extrapolation-end histories))))


(deftest part-2
  (is (= 2 (sut/sum-extrapolation-beg sample)))
  (is (= 990 (sut/sum-extrapolation-beg histories))))

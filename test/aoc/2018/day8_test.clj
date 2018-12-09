(ns aoc.2018.day8-test
  (:require [clojure.test :refer :all]
            [aoc.2018.day8 :as dut]))

(def input
  (-> "resources/2018/day8.input.txt"
      slurp
      clojure.string/split-lines
      first
      (as-> nbrs (str "[" nbrs "]"))
      read-string))

(def sample-input [2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2])

(deftest sum-finished-parents-test
  (is (= [138 [] []]
         (dut/sum-finished-parents 132 [[0 3] [0 1]] (drop 0 [2 1 1 2])))))

(deftest part-a-sample-test
  (is (= 138 (dut/part-a sample-input))))

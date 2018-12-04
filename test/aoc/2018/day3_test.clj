(ns aoc.2018.day3-test
  (:require [clojure.test :refer :all]
            [aoc.2018.day3 :as dut]))

(def input
  (->> "resources/2018/day3.input.txt"
       slurp
       clojure.string/split-lines))

(def part-a-test-input
  ["#1 @ 1,3: 4x4"
   "#2 @ 3,1: 4x4"
   "#3 @ 5,5: 2x2"])

(deftest overlap-test-1
  (is (= #{[3 3] [4 3]
           [3 4] [4 4]}
         (dut/overlap [1 1 3 4 4] [2 3 1 4 4]))))

(deftest overlap-test-4
  (is 
    (empty?
      (dut/overlap [1 1 3 4 4] [3 5 5 2 2]))))

(deftest overlap-test-3
  (is 
    (empty?
      (dut/overlap [2 3 1 4 4] [3 5 5 2 2]))))

(deftest overlap-test-2
  (is (= #{[3 3] [4 3]
           [3 4] [4 4]}
         (dut/overlap [2 3 1 4 4] [1 1 3 4 4]))))

(deftest parse-claim-test
  (is (= [123 655 494 12 23]
         (dut/parse-claim "#123 @ 655,494: 12x23"))))

(deftest part-a-sample-test
  (is (= 4
         (dut/total-overlap part-a-test-input))))

(deftest part-a-test
  (is (= 5 
         (dut/total-overlap input))))

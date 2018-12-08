(ns aoc.2018.day7-test
  (:require [clojure.test :refer :all]
            [aoc.2018.day7 :as dut]))

(def input
  (-> "resources/2018/day7.input.txt"
      slurp
      clojure.string/split-lines))

(def sample-input
  ["Step C must be finished before step A can begin."
   "Step C must be finished before step F can begin."
   "Step A must be finished before step B can begin."
   "Step A must be finished before step D can begin."
   "Step B must be finished before step E can begin."
   "Step D must be finished before step E can begin."
   "Step F must be finished before step E can begin."])


(deftest parta-sample-test
  (is (= "CABDFE"
         (dut/part-a sample-input))))

(deftest parta-test
  (is (= "HPDTNXYLOCGEQSIMABZKRUWVFJ"
         (dut/part-a input))))

(deftest partb-test
  (is (= 908
         (dut/part-b input))))

(deftest partb-sample-test
  (is (= 253
         (dut/part-b sample-input))))


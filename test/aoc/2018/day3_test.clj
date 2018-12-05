(ns aoc.2018.day3-test
  (:require [clojure.test :refer :all]
            [aoc.2018.day3 :as dut]))

(def input
  (-> "resources/2018/day3.input.txt"
      slurp
      (clojure.string/replace #"[#@,:x]" " ")
      clojure.string/split-lines))

(def part-a-sample-input
  ["#1 @ 1,3: 4x4"
   "#2 @ 3,1: 4x4"
   "#3 @ 5,5: 2x2"])


(deftest parse-claim-test
  (is (= [123 655 494 12 23]
         (dut/parse-claim "#123 @ 655,494: 12x23"))))

(deftest part-a-sample-test
  (is (= 4
         (dut/total-overlap part-a-sample-input))))

(deftest part-a-test
  (is (= 5 
         (dut/total-overlap input))))



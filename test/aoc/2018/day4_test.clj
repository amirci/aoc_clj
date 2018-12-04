(ns aoc.2018.day4-test
  (:require [clojure.test :refer :all]
            [aoc.2018.day4 :as dut]))

(def input
  (->> "resources/2018/day4.input.txt"
       slurp
       clojure.string/split-lines))



(dut/find-guard-most-asleep (take 50 input))


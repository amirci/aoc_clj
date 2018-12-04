(ns aoc.2018.day4-test
  (:require [clojure.test :refer :all]
            [aoc.2018.day4 :as dut]))

(def input
  (->> "resources/2018/day4.input.txt"
       slurp
       clojure.string/split-lines))

(def test-input 
  ["[1518-11-01 00:00] Guard #10 begins shift"
   "[1518-11-01 00:05] falls asleep"
   "[1518-11-01 00:25] wakes up"
   "[1518-11-01 00:30] falls asleep"
   "[1518-11-01 00:55] wakes up"
   "[1518-11-01 23:58] Guard #99 begins shift"
   "[1518-11-02 00:40] falls asleep"
   "[1518-11-02 00:50] wakes up"
   "[1518-11-03 00:05] Guard #10 begins shift"
   "[1518-11-03 00:24] falls asleep"
   "[1518-11-03 00:29] wakes up"
   "[1518-11-04 00:02] Guard #99 begins shift"
   "[1518-11-04 00:36] falls asleep"
   "[1518-11-04 00:46] wakes up"
   "[1518-11-05 00:03] Guard #99 begins shift"
   "[1518-11-05 00:45] falls asleep"
   "[1518-11-05 00:55] wakes up"])

(deftest part-a-sample-test
  (is (= 240 (dut/part-a test-input))))

(deftest part-a-test
  (is (= 102688 (dut/part-a input))))

(deftest part-b-sample-test
  (is (= 4455 (dut/part-b test-input))))

(deftest part-b-test
  (is (= 56901 (dut/part-b input))))



(ns aoc.2019.day7-test
  (:require
   [aoc.2019.day7 :as sut]
   [clojure.test :refer [testing]]
   [clojure.test.check.clojure-test :refer [defspec]]
   [clojure.test.check.generators :as g]
   [clojure.test.check.properties :as prop]
   [clojure.java.io :as io]
   [clojure.edn :as edn]
   [clojure.test :as t :refer [deftest is]]))


(def input (io/resource "2019/day7.input.txt"))

(def intcode
  (->> input
       slurp
       (format "[%s]")
       edn/read-string))

(def samples-a
  {43210 [[4 3 2 1 0] [3 15 3 16 1002 16 10 16 1 16 15 15 4 15 99 0 0]]
   54321 [[0 1 2 3 4] [3 23 3 24 1002 24 10 24 1002 23 -1 23 101 5 23 23 1 24 23 23 4 23 99 0 0]]
   65210 [[1 0 4 3 2] [3 31 3 32 1002 32 10 32 1001 31 -2 31 1007 31 0 33 1002 33 7 33 1 33 31 31 1 32 31 31 4 31 99 0 0 0]]})

(deftest part-a-samples
  (doseq [[expected [cfg code]] samples-a]
    (testing (str "max thruster signal should be " expected " using " cfg)
      (is (= expected
             (sut/thruster-signal code cfg))))))

(deftest part-a-samples-with-max
  (doseq [[expected [cfg code]] (take 2 samples-a )]
    (testing (str "max thruster signal should be " expected)
      (is (= [expected cfg] (sut/max-thruster-signal code ))))))

(deftest part-a-test
  (is (= [65464 [0 3 4 2 1]] (sut/max-thruster-signal intcode))))








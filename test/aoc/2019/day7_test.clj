(ns aoc.2019.day7-test
  (:require
   [aoc.2019.day5 :as day5]
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
       clojure.string/split-lines
       first
       (format "[%s]")
       edn/read-string))

(def samples-a
  {43210 [[4 3 2 1 0] [3 15 3 16 1002 16 10 16 1 16 15 15 4 15 99 0 0]]
   54321 [[0 1 2 3 4] [3 23 3 24 1002 24 10 24 1002 23 -1 23 101 5 23 23 1 24 23 23 4 23 99 0 0]]
   65210 [[1 0 4 3 2] [3 31 3 32 1002 32 10 32 1001 31 -2 31 1007 31 0 33 1002 33 7 33 1 33 31 31 1 32 31 31 4 31 99 0 0 0]]})

(deftest part-a-samples
  (doseq [[expected [cfg code]] samples-a]
    (testing (str "thruster signal should be " expected " using " cfg)
      (is (= expected
             (sut/thruster-signal code cfg))))))

(deftest part-a-samples-with-max
  (doseq [[expected [cfg code]] samples-a]
    (testing (str "max thruster signal should be " expected)
      (is (= [expected cfg] (sut/max-thruster-signal code ))))))

(deftest part-a-test
  (is (= [65464 [0 3 4 2 1]] (sut/max-thruster-signal intcode))))

(def s1-code [3 26          ;  0: a26 <- read-input
              1001 26 -4 26 ;  2: a26 = a26 + -4
              3 27          ;  6: a27 <- read-input 
              1002 27 2 27  ;  8: a27 = a27 * 2
              1 27 26 27    ; 12: a27 = a27 + a26
              4 27          ; 16: ouput <- a27
              1001 28 -1 28 ; 18: a28 = a28 + -1
              1005 28 6     ; 22: jump-nz a28 6
              99            ; 25: halt
              0 0 5])


(def samples-b
  {139629729 [[9 8 7 6 5] s1-code]
   18216 [[9 7 8 5 6] [3 52 1001 52 -5 52 3 53 1 52 56 54 1007 54 5 55 1005 55 26 1001 54 -5 54 1105 1 12 1 53 54 53 1008 54 0 55 1001 55 1 55 2 53 55 53 4 53 1001 56 -1 56 1005 56 6 99 0 0 0 0 10]]})


(deftest samples-part-b-test
  (doseq [[expected [cfg code]] samples-b]
    (is (= expected
          (sut/thruster-signal-in-loop-async code cfg)))))

(deftest samples-part-b-max-test
  (doseq [[expected [cfg code]] samples-b]
    (is (= [cfg expected]
           (sut/max-thruster-signal-in-loop code)))))

(deftest part-b-test
  (is (= [[7 9 5 6 8] 1518124]
         (sut/max-thruster-signal-in-loop intcode))))

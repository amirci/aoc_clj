(ns aoc.2017.day19-test
  (:require [aoc.2017.day19 :as sut]
            [clojure.java.io :as io]
            [clojure.test :refer [deftest testing is]]))


(def input
  (->> "2017/day19.input.txt"
       io/resource
       slurp
       clojure.string/split-lines))


(def sample
  ["    |          "
   "    |  +--+    "
   "    A  |  C    "
   "F---|----E|--+ "
   "    |  |  |  D "
   "    +B-+  +--+"])


(deftest part-a
  (testing "sample"
    (is (= "ABCDEF" (sut/follow-routing sample))))

  (testing "input"
    (is (= "SXPZDFJNRL" (sut/follow-routing input)))))


(deftest part-b
  (testing "sample"
    (is (= 38 (sut/count-routing-steps sample))))

  (testing "input"
    (is (= 18126 (sut/count-routing-steps input)))))

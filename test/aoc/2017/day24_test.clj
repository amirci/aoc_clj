(ns aoc.2017.day24-test
  (:require [aoc.2017.day24 :as sut]
            [clojure.java.io :as io]
            [clojure.test :refer [deftest testing is]]))

(def input
  (->> "2017/day24.input.txt"
       io/resource
       slurp
       clojure.string/split-lines
       (map #(clojure.string/split % #"/"))
       (map (partial map read-string))))


(def sample
  [[0 2]
   [2 2]
   [2 3]
   [3 4]
   [3 5]
   [0 1]
   [10 1]
   [9 10]])


(deftest part-a
  (testing "sample"
    (is (= 31 (sut/strongest-bridge sample))))

  (testing "input"
    (is (= 1940 (sut/strongest-bridge input)))))

(deftest part-b
  (testing "sample"
    (is (= 19 (sut/longest-bridge sample))))

  (testing "sample"
    (is (= 1928 (sut/longest-bridge input)))))


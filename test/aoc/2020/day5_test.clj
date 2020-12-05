(ns aoc.2020.day5-test
  (:require [aoc.2020.day5 :as sut]
            [clojure.java.io :as io]
            [clojure.test :as t :refer :all]))

(def input
  (->> "2020/day5.input.txt"
       io/resource
       slurp
       clojure.string/split-lines))


(deftest part-a
  (testing "examples"
    (is (= [70, 7, 567] (sut/calc-seat "BFFFBBFRRR")))
    (is (= [14, 7, 119] (sut/calc-seat "FFFBBBFRRR")))
    (is (= [102, 4, 820] (sut/calc-seat "BBFFBBFRLL"))))

  (testing "input"
    (is (= 926 (sut/highest-seat-id input)))))


(deftest part-b
  (testing "input"
    (is (= 657 (sut/missing-id input)))))


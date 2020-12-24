(ns aoc.2020.day8-test
  (:require [aoc.2020.day8 :as sut]
            [clojure.java.io :as io]
            [clojure.test :as t :refer :all]))

(def input
  (->> "2020/day8.input.txt"
       io/resource
       slurp
       clojure.string/split-lines))



(def sample-a
  ["nop +0"
   "acc +1"
   "jmp +4"
   "acc +3"
   "jmp -3"
   "acc -99"
   "acc +1"
   "jmp -4"
   "acc +6"])


(deftest part-a
  (testing "sample"
    (is (= 5
           (sut/run-until-twice sample-a))))
  (testing "input"
    (is (= 1217
           (sut/run-until-twice input)))))


(def program-a (sut/parse sample-a))


(deftest part-b
  (testing "sample"
    (is (= 8
           (sut/corrupt sample-a))))
  (testing "input"
    (is (= 501
           (sut/corrupt input)))))

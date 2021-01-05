(ns aoc.2017.day23-test
  (:require [aoc.2017.day23 :as sut]
            [clojure.java.io :as io]
            [clojure.test :refer [deftest testing is]]))

(def input
  (->> "2017/day23.input.txt"
       io/resource
       slurp
       clojure.string/split-lines))



(deftest part-a
  (testing "input"
    (is (= 6241 (:mul (sut/run-code input))))))


(deftest part-b
  (testing "input"
    (is (= 909 (sut/program 81)))))


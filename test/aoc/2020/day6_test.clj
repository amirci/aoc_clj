(ns aoc.2020.day6-test
  (:require [aoc.2020.day6 :as sut]
            [clojure.java.io :as io]
            [clojure.test :as t :refer :all]))

(def input
  (->> "2020/day6.input.txt"
       io/resource
       slurp
       clojure.string/split-lines))


(deftest part-a
  (testing "input"
    (is (= 6585 (sut/single-answers input)))))

(deftest part-b
  (testing "input"
    (is (= 3276 (sut/common-answers input)))))

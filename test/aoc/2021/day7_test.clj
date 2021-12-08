(ns aoc.2021.day7-test
  (:require [aoc.2021.day7 :as sut]
            [clojure.test :as t :refer [deftest is]]
            [clojure.string :as st]))

(def input
  (-> "resources/2021/day7.txt"
      slurp
      st/split-lines
      first
      (st/replace "," " ")
      (#(format "[%s]" %))
      read-string))



(def sample
  [16,1,2,0,4,2,7,1,2,14])


(deftest part-1
  (is (= 37 (sut/calc-fuel sample)))
  (is (= 355764 (sut/calc-fuel input))) )

(deftest part-2
  (is (= 168 (sut/calc-fuel2 sample)))
  (is (= 99634572 (sut/calc-fuel2 input))) )

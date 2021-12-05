(ns aoc.2021.day6-test
  (:require [aoc.2021.day6 :as sut]
            [clojure.test :as t :refer [deftest is]]
            [clojure.string :as st]))

(def input
  (-> "resources/2021/day6.txt"
      slurp
      st/split-lines
      first
      (st/replace "," " ")
      (#(format "[%s]" %))
      read-string))


(def sample [3,4,3,1,2])



(deftest part-1
  (is (= 5934 (sut/simulate sample 80)))
  (is (= 345793 (sut/simulate input 80))))


(deftest part-2
  (is (= 26984457539 (sut/simulate2 sample 256)))
  (is (= 1572643095893 (sut/simulate2 input 256))))


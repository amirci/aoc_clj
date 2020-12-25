(ns aoc.2020.day25-test
  (:require [aoc.2020.day25 :as sut]
            [clojure.test :as t :refer :all]))


(def input
  {:card 13316116 :door 13651422})


(def sample
  {:card 5764801 :door 17807724})


(deftest part-a
  (testing "sample"
    (is (= 8 (sut/find-loop-cycle (:card sample))))
    (is (= 11 (sut/find-loop-cycle (:door sample)))) )

  (testing "input"
    (is (= 620544  (sut/find-loop-cycle (:card input))))
    (is (= 3539147 (sut/find-loop-cycle (:door input))))
    (is (= 12929 (sut/encryption-key input)))))


(deftest part-b
  )



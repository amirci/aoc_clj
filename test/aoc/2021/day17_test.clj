(ns aoc.2021.day17-test
  (:require [aoc.2021.day17 :as sut]
            [clojure.test :as t :refer [deftest is]]))


(def target
  "target area: x=60..94, y=-171..-136"
  [[60 94] [-136 -171]])


(def test-target
  "target area x=10..20, y=-10..-5"
  [[20 30] [-5 -10]])


(deftest part-1
  (is (= 45 (sut/highest test-target)))
  (is (= 14535 (sut/highest target))))


(deftest part-2
  (is (= 112 (sut/count-all-vs test-target)))
  (is (= 2270 (sut/count-all-vs target))))

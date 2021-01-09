(ns aoc.2017.day25-test
  (:require [aoc.2017.day25 :as sut]
            [clojure.java.io :as io]
            [clojure.test :refer [deftest testing is]]))


(def input
  {:A [[1 :right :B] [0 :left  :E]]
   :B [[1 :left  :C] [0 :right :A]]
   :C [[1 :left  :D] [0 :right :C]]
   :D [[1 :left  :E] [0 :left  :F]]
   :E [[1 :left  :A] [1 :left  :C]]
   :F [[1 :left  :E] [1 :right :A]]})


(def sample
  {:A [[1 :right :B] [0 :left  :B]]
   :B [[1 :left  :A] [1 :right :A]]})


(def steps 12208951)

(def starting-state :A)

(deftest part-a
  (testing "sample"
    (is (= 3 (sut/checksum sample :A 6))))

  (testing "input"
    (is (= 4387 (sut/checksum input :A steps)))) )


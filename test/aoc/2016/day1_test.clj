(ns aoc.2016.day1-test
  (:require [clojure.test :refer :all]
            [aoc.2016.day1 :refer :all]))


(def instructions
  (-> "resources/2016/day1.input.txt"
      slurp
      (clojure.string/split #"\n")
      first
      (clojure.string/split #",\ +")))

(deftest sample1
  (is (= 5 (blocks-away ["R2" "L3"]))))

(deftest sample2
  (is (= 2 (blocks-away ["R2" "R2" "R2"]))))

(deftest sample3
  (is (= 12 (blocks-away ["R5" "L5" "R5" "R3"]))))

(deftest part-a
  (is (= 273 (blocks-away instructions))))

(deftest duplicate-test
  (is (= 4 (first-repeated ["R8" "R4" "R4" "R8"]))))

(deftest part-b
  (is (= 115 (first-repeated instructions))))


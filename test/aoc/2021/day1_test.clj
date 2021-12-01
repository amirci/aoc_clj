(ns aoc.2021.day1-test
  (:require [aoc.2021.day1 :as sut]
            [clojure.test :refer [deftest is] :as t]))

(def measures
  (->> "resources/2021/day1.txt"
       slurp
       clojure.string/split-lines
       (map read-string)))

(def sample
  [199
   200
   208
   210
   200
   207
   240
   269
   260
   263])


(deftest part-1
  (is (= 7 (sut/larger-than-previous sample)))
  (is (= 1475 (sut/larger-than-previous measures))))


(deftest part-2
  (is (= 5 (sut/larger-windows sample)))
  (is (= 1516 (sut/larger-windows measures))))


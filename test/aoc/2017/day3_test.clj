(ns aoc.2017.day3-test
  (:require [clojure.test :refer :all]
            [aoc.2017.day3 :refer :all]))



(deftest part-a
  (->> 277678
       path-to-center-spiral
       (= 475)
       is))

(deftest part-b
  (->> 277678
       find-larger-in-spiral-b
       (= 279138)
       is))

(ns aoc.2017.day6-test
  (:require [clojure.test :refer :all]
            [aoc.2017.day6 :refer :all]))

(def input
  (-> "resources/2017/day6.input.txt"
      slurp
      clojure.string/split-lines
      first
      (as-> i 
        (read-string (str "[" i "]")))))

(deftest part-a
  (-> input
      find-first-duplicate-config-length
      (= 4074)
      is))


(deftest part-b
  (-> input
      find-loop-length
      (= 2793)
      is))



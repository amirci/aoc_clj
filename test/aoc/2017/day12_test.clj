(ns aoc.2017.day12-test
  (:require [clojure.test :refer :all]
            [aoc.2017.day12 :refer :all]))


(def sample-input ["0 <-> 2"
                   "1 <-> 1"
                   "2 <-> 0, 3, 4"
                   "3 <-> 2, 4"
                   "4 <-> 2, 3, 6"
                   "5 <-> 6"
                   "6 <-> 4, 5"])

(def input 
  (-> "resources/2017/day12.input.txt"
      slurp
      clojure.string/split-lines
      load-programs))

(deftest part-a
  (is (= 128 (count (collect-grp 0 input)))))

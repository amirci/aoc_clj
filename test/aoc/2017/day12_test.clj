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

(def graph 
  (-> "resources/2017/day12.input.txt"
      slurp
      clojure.string/split-lines
      load-programs))

(deftest part-a
  (is (= 128 (count (collect-group 0 graph)))))

(deftest part-b
  (is (= 209 (count-distinct-groups graph))))

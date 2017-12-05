(ns aoc.2017.day5-test
  (:require [clojure.test :refer :all]
            [aoc.2017.day5 :refer :all]))


(deftest part-a
  (->> "resources/2017/day5.input.txt"
       slurp
       clojure.string/split-lines
       count-steps-to-exist-maze
       (= 318883)
       is))

(deftest part-b
  (->> "resources/2017/day5.input.txt"
       slurp
       clojure.string/split-lines
       count-steps-to-exist-maze-b
       (= 23948711)
       is))



(ns aoc.2016.day22-test
  (:require [clojure.test :refer :all]
            [aoc.2016.day22 :refer :all]))


(def instructions
  (->> "2016/day22.input.txt"
      clojure.java.io/resource
      slurp
      clojure.string/split-lines
      (drop 2)))

; rank 360
(deftest part-a
  (is (= 937 (viable-nodes instructions))))

(deftest part-b
  (is (= 188 shortest-moves)))


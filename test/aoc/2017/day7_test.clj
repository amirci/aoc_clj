(ns aoc.2017.day7-test
  (:require [clojure.test :refer :all]
            [aoc.2017.day7 :refer :all]))

(def input
  (-> "resources/2017/day7.input.txt"
      slurp
      clojure.string/split-lines))

(def sample-input ["pbga (66)"
                   "xhth (57)"
                   "ebii (61)"
                   "havc (66)"
                   "ktlj (57)"
                   "fwft (72) -> ktlj, cntj, xhth"
                   "qoyq (66)"
                   "padx (45) -> pbga, havc, qoyq"
                   "tknk (41) -> ugml, padx, fwft"
                   "jptl (61)"
                   "ugml (68) -> gyxo, ebii, jptl"
                   "gyxo (61)"
                   "cntj (57)"])

(deftest sample-part-a
  (-> sample-input
      find-root
      (= :tknk)
      is))

(deftest part-a
  (-> input
      find-root
      (= :mkxke)
      is))

(deftest sample-part-b
  (-> sample-input
      find-unbalanced-weight
      (= 60)
      is))

(deftest part-b
  (-> input
      find-unbalanced-weight
      (= 268)
      is))




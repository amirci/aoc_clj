(ns aoc.2017.day18-test
  (:require [clojure.test :refer :all]
            [aoc.2017.day18 :refer :all]))

(def instructions ["set a 1"
                   "add a 2"
                   "mul a a"
                   "mod a 5"
                   "snd a"
                   "set a 0"
                   "rcv a"
                   "jgz a -1"
                   "set a 1"
                   "jgz a -2"])

(deftest example-part-a
  (is (= 4 (find-first-rcv instructions))))

(def input (-> "resources/2017/day18.input.txt"
               slurp
               clojure.string/split-lines))

(deftest part-a
  (is (= 4601 (find-first-rcv input))))

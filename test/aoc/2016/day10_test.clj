(ns aoc.2016.day10-test
  (:require [clojure.test :refer :all]
            [aoc.2016.day10 :refer :all]))

(def instructions
  (-> "2016/day10.input.txt"
      clojure.java.io/resource
      slurp
      clojure.string/split-lines))


(deftest parse-tokens-test
  (is (= [{"2" [5]} {} []] (parse-cmd [{} {} []] "value 5 goes to bot 2"))))


(deftest parse-tokens-test-2
  (is (= [{"0" [5] "1" [2 3] "2" [2 5]} {} []] 
         (parse-cmd [{"1" [3] "2" [2 5]} {} []] "bot 2 gives low to bot 1 and high to bot 0"))))

(def sample-instructions
  ["value 5 goes to bot 2"
   "bot 2 gives low to bot 1 and high to bot 0"
   "value 3 goes to bot 1"
   "bot 1 gives low to output 1 and high to bot 0"
   "bot 0 gives low to output 2 and high to output 0"
   "value 2 goes to bot 2"])

(deftest load-commands-test
  (is (= "2" (find-bot [2 5] sample-instructions))))

(deftest part-a
  (is (= "86" (find-bot [17 61] instructions))))

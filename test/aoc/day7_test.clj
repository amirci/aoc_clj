(ns aoc.day7-test
  (:require [clojure.test :refer :all]
            [aoc.day7 :refer :all]))


(deftest instructions-in-order
 (let [instructions ["123 -> x"
                     "456 -> y"
                    "x AND y -> d"
                    "x OR y -> e"
                    "x LSHIFT 2 -> f"
                    "y RSHIFT 2 -> g"
                    "NOT x -> h"
                    "NOT y -> i"]
       board (load-board instructions)]

  (is (= {"x" 123 "y" 456 "d" 72  "e" 507 "f" 492 "g" 114 "h" 65412 "i" 65079}
         board))))

(deftest and-with-number
  (let [board (load-board ["456 -> y" "123 AND y -> d"])]
       (is (= {"y" 456 "d" 72} board))))

(deftest assign-circuit-to-circuit
  (let [board (load-board ["456 -> y" "y -> d"])]
       (is (= {"y" 456 "d" 456} board))))
  
(deftest instructions-no-order
 (let [instructions ["123 -> x"
                    "x AND y -> d"
                    "x OR y -> e"
                    "x LSHIFT 2 -> f"
                    "y RSHIFT 2 -> g"
                    "NOT x -> h"
                     "456 -> y"
                    "NOT y -> i"]
       board (load-board instructions)]
       (is (= {"x" 123 "y" 456 "d" 72  "e" 507 "f" 492 "g" 114 "h" 65412 "i" 65079}
              board))))


(deftest part-a
  (let [instructions (clojure.string/split (slurp "resources/day7.input.txt") #"\n")
        board (load-board instructions)]
       (is (= 46065 (get board "a")))))

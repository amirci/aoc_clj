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
       board (mk-board instructions)]
  
  (is (= 123 (get board "x")))
  (is (= 456 (get board "y")))
  (is (= 72 (get board "d")))
  (is (= 507 (get board "e")))
  (is (= 492 (get board "f")))
  (is (= 114 (get board "g")))
  (is (= 65412 (get board "h")))
  (is (= 65079 (get board "i")))
  ))


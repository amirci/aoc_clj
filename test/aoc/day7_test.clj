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
 
  (is (= {"x" 123 "y" 456 "d" 72  "e" 507 "f" 492 "g" 114 "h" 65412 "i" 65079}
         board))))

(deftest and-with-number
  (let [board (mk-board ["456 -> y" "123 AND y -> d"])]
       (is (= {"y" 456 "d" 72} board))))

;(deftest instructions-no-order
; (let [instructions ["123 -> x"
;                    "x AND y -> d"
;                    "x OR y -> e"
;                    "x LSHIFT 2 -> f"
;                    "y RSHIFT 2 -> g"
;                    "NOT x -> h"
;                     "456 -> y"
;                    "NOT y -> i"]
;       board (mk-board instructions)]
;  (is (= 123 (circuit board "x")))
;  (is (= 456 (circuit board "y")))
;  (is (= 72 (circuit board "d")))
;  (is (= 507 (circuit board "e")))
;  (is (= 492 (circuit board "f")))
;  (is (= 114 (circuit board "g")))
;  (is (= 65412 (circuit board "h")))
;  (is (= 65079 (circuit board "i")))))


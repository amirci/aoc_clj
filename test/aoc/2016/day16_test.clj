(ns aoc.2016.day16-test
  (:require [clojure.test :refer :all]
            [aoc.2016.day16 :refer :all]))

(deftest part-a-sample-test
  (is (= (seq "10000011110010000111") (dragon-curve "10000" 20))))

(deftest part-a-sample-checksum-test
  (is (= "01100" (odd-checksum2 "10000" 20))))

; rank 425
(deftest part-a
  (is (= "10010100110011100" (time (odd-checksum init-state 272)))))

; rang 728
;(deftest part-b
;  (is (= "01100100101101100" (time (odd-checksum init-state 35651584)))))


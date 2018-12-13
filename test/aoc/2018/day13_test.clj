(ns aoc.2018.day13-test
  (:require [clojure.test :refer :all]
            [aoc.2018.day13 :as dut]))

(def input
  (->> "resources/2018/day13.input.txt"
       slurp
       clojure.string/split-lines))

(def sample-input
  ["/->-\\        "
   "|   |  /----\\"
   "| /-+--+-\\  |"
   "| | |  | v  |"
   "\\-+-/  \\-+--/"
   "  \\------/  "])

(deftest part-a-sample-test
  (is (= [7 3] (dut/part-a sample-input))))

(deftest part-a-test
  (is (= [111 13] (dut/part-a input))))


(def sample-input-b
  [" />-<\\ "
   "|   |  "
   "| /<+-\\"
   "| | | v"
   "\\>+</ |"
   "  |   ^"
   "  \\<->/"])

;(deftest part-b-sample-test
;  (is (= [6 4] (dut/part-b sample-input-b))))

(deftest part-b-test
  (is (= [0 0] (dut/part-b input))))


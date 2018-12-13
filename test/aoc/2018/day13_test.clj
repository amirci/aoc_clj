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

(def pp (dut/parse-instructions sample-input))


(first pp)
(second pp)

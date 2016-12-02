(ns aoc.2016.day2-test
  (:require [clojure.test :refer :all]
            [aoc.2016.day2 :refer :all]))

(def instructions
  (-> "resources/2016/day2.input.txt"
      slurp
      (clojure.string/split #"\n")))

(deftest sample1
  (is (= "1985" (bathroom-code ["ULL" "RRDDD" "LURDL" "UUUUD"]))))

(deftest part-a
  (is (= "65556" (bathroom-code instructions))))

; swap keycode for keycode 2
;(deftest part-b
; (is (= "CB779" (bathroom-code instructions))))

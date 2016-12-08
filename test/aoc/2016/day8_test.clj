(ns aoc.2016.day8-test
  (:require [clojure.test :refer :all]
            [aoc.2016.day8 :refer :all]))

(def instructions
  (-> "resources/2016/day8.input.txt"
      slurp
      clojure.string/split-lines))


(deftest part-a-sample-rect
  (let [screen (empty-screen 7 3)
        actual (parse ["rect" "3x2" screen])
        expected [[\# \# \# \. \. \. \.]
                  [\# \# \# \. \. \. \.]
                  [\. \. \. \. \. \. \.]]]
    (is (= actual expected))))


(deftest part-a-rotate-column
  (let [screen [[\# \# \# \. \. \. \.]
                [\# \# \# \. \. \. \.]
                [\. \. \. \. \. \. \.]]
        expected [[\# \. \# \. \. \. \.]
                  [\# \# \# \. \. \. \.]
                  [\. \# \. \. \. \. \.]]
        actual (parse ["rotate" "column" "x=1" "by" "1" screen])]
    (is (= actual expected))))

(deftest part-a-rotate-row
  (let [screen [[\# \. \# \. \. \. \.]
                [\# \# \# \. \. \. \.]
                [\. \# \. \. \. \. \.]]
        expected [[\. \. \. \. \# \. \#]
                  [\# \# \# \. \. \. \.]
                  [\. \# \. \. \. \. \.]]
        actual (parse ["rotate" "row" "y=0" "by" "4" screen])]
    (is (= expected actual))))

(deftest part-a-rotate-column-2
  (let [screen [[\. \. \. \. \# \. \#]
                [\# \# \# \. \. \. \.]
                [\. \# \. \. \. \. \.]]
        expected [[\. \# \. \. \# \. \#]
                  [\# \. \# \. \. \. \.]
                  [\. \# \. \. \. \. \.]]
        actual (parse ["rotate" "column" "x=1" "by" "1" screen])]
    (is (= actual expected))))

(deftest part-a
  (is (= 115 (count-pixels instructions))))


; EFEYKFRFIJ
; part-b
(print-screen instructions)

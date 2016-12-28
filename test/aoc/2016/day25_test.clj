(ns aoc.2016.day25-test
  (:require [clojure.test :refer :all]
            [aoc.2016.day25 :refer :all]))


(def instructions
  (->> "2016/day25.input.txt"
      clojure.java.io/resource
      slurp
      clojure.string/split-lines
      vec))

; 29 secs
;(deftest part-a
;  (is (= 182 (time (find-pattern "0101010101" instructions)))))

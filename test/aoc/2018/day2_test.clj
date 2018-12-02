(ns aoc.2018.day2-test
  (:require [clojure.test :refer :all]
            [aoc.2018.day2 :as d2]))

(def input 
  (->> "resources/2018/day2.input.txt"
       slurp
       clojure.string/split-lines))

(deftest part-a-test
  (is (= 6972 (d2/checksum input))))


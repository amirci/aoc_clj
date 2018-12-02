(ns aoc.2018.day2-test
  (:require [clojure.test :refer :all]
            [aoc.2018.day2 :as d2]))

(def input 
  (->> "resources/2018/day2.input.txt"
       slurp
       clojure.string/split-lines))

(deftest part-a-test
  (is (= 6972 (d2/part-a input))))

(deftest part-b-test
  (is (= "aixwcbzrmdvpsjfgllthdyoqe" (d2/part-b input))))

(def box ["abcde"
         "fghij"
         "klmno"
         "pqrst"
         "fguij"
         "axcye"
         "wvxyz"])

(deftest part-b-sample-test
  (is (= "fgij" (d2/part-b box))))


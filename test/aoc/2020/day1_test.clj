(ns aoc.2020.day1-test
  (:require [aoc.2020.day1 :as sut]
            [clojure.java.io :as io]
            [clojure.edn :as edn]
            [clojure.test :as t :refer [deftest is]]))

(def input
  (->> "2020/day1.input.txt"
       io/resource
       slurp
       clojure.string/split-lines
       (map edn/read-string)))


(def example-a
  [1721
   979
   366
   299
   675
   1456])

(deftest part-a
  (is (= 514579 (sut/fix-report example-a)))
  (is (= 542619 (sut/fix-report input))))


(deftest part-b
  (is (= 241861950 (sut/fix-report-3 example-a)))
  (is (= 32858450 (sut/fix-report-3 input))))


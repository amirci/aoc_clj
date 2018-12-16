(ns aoc.2018.day6-test
  (:require [clojure.test :refer :all]
            [aoc.2018.day6 :as dut]))

(def input
  (-> "resources/2018/day6.input.txt"
      slurp
      clojure.string/split-lines
      (->>
        (map #(read-string (str "[" % "]"))))))

(def sample-input [[1 1] [1 6] [8 3] [3 4] [5 5] [8 9]])

(deftest calc-boundaries-test
  (is (= [1 1 8 9] (dut/calc-boundaries sample-input))))

(deftest all-points-test
  (is (= #{[1 1] [2 1] [2 2] [1 2]} (set (dut/all-points-in [1 1 2 2])))))

(deftest closest-point-test
  (is (= [1 1] (dut/closest-point sample-input [0 0]))))

(deftest closest-point-is-nil-test
  (is (= nil (dut/closest-point sample-input [5 0]))))

(deftest part-a-sample-test
  (is (= 17 (dut/part-a sample-input))))

(deftest part-a-test
  (is (= 3604
         (dut/part-a input))))

(deftest part-b-sample-test
  (is (= 16 (dut/part-b sample-input 32))))

(deftest part-b-test
  (is (= 46563 (dut/part-b input 10000))))

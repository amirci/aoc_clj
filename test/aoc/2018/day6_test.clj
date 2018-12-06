(ns aoc.2018.day6-test
  (:require [clojure.test :refer :all]
            [aoc.2018.day6 :as dut]))

(def input
  (-> "resources/2018/day6.input.txt"
      slurp
      (clojure.string/replace #"[,]" " ")
      clojure.string/split-lines))

(def sample-input
  (->> [[1 1] [1 6] [8 3] [3 4] [5 5] [8 9]]
       (map #(vector % #{}))
       (into {})))

(deftest min-dist-test
  (is (= [1 1]
         (dut/min-dist [0 0] (keys sample-input)))))

(deftest min-dist-test-dont-count
  (is
    (= nil
       (dut/min-dist [5 0] (keys sample-input)))))

(deftest part-a-sample-test
  (is (= 17 (dut/part-a sample-input))))

;(deftest part-a-test
;  (is (= 5 (dut/part-a (dut/->map-pts input) 400 400))))


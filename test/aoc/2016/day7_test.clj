(ns aoc.2016.day7-test
  (:require [clojure.test :refer :all]
            [aoc.2016.day7 :refer :all]))

(def instructions
  (-> "resources/2016/day7.input.txt"
      slurp
      clojure.string/split-lines))

(deftest part-a-samples
  (testing "sample1"
    (is (tls? "abba[mnop]qrst")))
  (testing "sample2 not tls"
    (is (not (tls? "abcd[bddb]xyyx")))))

; rank 362 at 38 min
(deftest part-a
  (is (= 115 (count-tls instructions))))

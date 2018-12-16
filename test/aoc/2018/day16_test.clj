(ns aoc.2018.day16-test
  (:require [clojure.test :refer :all]
            [aoc.2018.day16 :as dut]))

(def input
  (-> "resources/2018/day16.input.txt"
      slurp
      (clojure.string/replace "Before:" "")
      (clojure.string/replace "After:" "")
      clojure.string/split-lines
      (->>
        (take 3044)
        (remove empty?)
        (partition 3)
        (map dut/parse-before-after))))

;3047
(def program
  (->> "resources/2018/day16.input.txt"
       slurp
       clojure.string/split-lines
       (drop 3046)
       (map #(read-string (str "[" % "]")))))

(deftest part-a-test
  (is (= 521 (dut/part-a input))))

(deftest part-b-test
  (is (= 594 (dut/part-b input program))))

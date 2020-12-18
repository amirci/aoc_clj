(ns aoc.2020.day18-test
  (:require [aoc.2020.day18 :as sut]
            [clojure.java.io :as io]
            [clojure.test :as t :refer :all]))

(def input
  (->> "2020/day18.input.txt"
       io/resource
       slurp
       clojure.string/split-lines))


(deftest part-a
  (testing "sample"
    (is (= 26 (sut/run-eval "2 * 3 + (4 * 5)")))
    (is (= 437 (sut/run-eval "5 + (8 * 3 + 9 + 3 * 4 * 3)")))
    (is (= 12240 (sut/run-eval "5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))")))
    (is (= 13632 (sut/run-eval "((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2"))))

  (testing "input"
    (is (= 31142189909908 (sut/sum-math input)))))


(deftest part-b
  (testing "sample"
    (is (= 51 (sut/run-eval-advanced "1 + (2 * 3) + (4 * (5 + 6))")))
    (is (= 46 (sut/run-eval-advanced "2 * 3 + (4 * 5)")))
    (is (= 1445 (sut/run-eval-advanced "5 + (8 * 3 + 9 + 3 * 4 * 3)")))
    (is (= 669060 (sut/run-eval-advanced "5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))")))
    (is (= 23340 (sut/run-eval-advanced "((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2"))))

  (testing "input"
    (is (= 323912478287549 (sut/sum-math-advanced input)))))

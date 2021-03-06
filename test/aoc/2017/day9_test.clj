(ns aoc.2017.day9-test
  (:require
    [clojure.test :refer :all]
    [blancas.kern.core :refer :all]
    [blancas.kern.lexer.basic :refer :all]
    [aoc.2017.day9 :refer :all]))

(def input
  (-> "resources/2017/day9.input.txt"
      slurp
      clojure.string/split-lines
      first))



(deftest sample-test-part-a
  (testing "multiple groups"
    (is (= 16 (group-score "{{{},{},{{}}}}"))))
  (testing "simple 1 group"
    (is (= 1 (group-score "{}")))))

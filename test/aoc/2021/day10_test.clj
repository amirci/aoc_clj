(ns aoc.2021.day10-test
  (:require [aoc.2021.day10 :as sut]
            [clojure.string :as st]
            [clojure.test :as t :refer [deftest is]]))

(def input
  (-> "resources/2021/day10.txt"
      slurp
      st/split-lines))


(def sample
  ["[({(<(())[]>[[{[]{<()<>>"
  "[(()[<>])]({[<{<<[]>>("
  "{([(<{}[<>[]}>{[]{[(<()>"
  "(((({<>}<{<{<>}{[]{[]{}"
  "[[<[([]))<([[{}[[()]]]"
  "[{[{({}]{}}([{[{{{}}([]"
  "{<[[]]>}<{[{[{[]{()[[[]"
  "[<(<(<(<{}))><([]([]()"
  "<{([([[(<>()){}]>(<<{{"
  "<{([{{}}[<[[[<>{}]]]>[]]"])

(def corrupted
  #{"{([(<{}[<>[]}>{[]{[(<()>"
    "[[<[([]))<([[{}[[()]]]"
    "[{[{({}]{}}([{[{{{}}([]"
    "[<(<(<(<{}))><([]([]()"
    "<{([([[(<>()){}]>(<<{{"})


(deftest part-1
  (is (= 26397 (sut/sum-illegal sample)))
  (is (= 392139 (sut/sum-illegal input))))


(deftest part-2
  (is (= 288957 (sut/autocomplete-winner sample)))
  (is (= 4001832844 (sut/autocomplete-winner input))))


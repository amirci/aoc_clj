(ns aoc.2021.day8-test
  (:require [aoc.2021.day8 :as sut]
            [clojure.string :as st]
            [clojure.test :as t :refer [deftest is]]))


(def input
  (-> "resources/2021/day8.txt"
      slurp
      st/split-lines))


(def sample
  ["be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe"
   "edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc"
   "fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg"
   "fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb"
   "aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea"
   "fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb"
   "dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe"
   "bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef"
   "egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb"
   "gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce"])



(deftest part-1
  (is (= 26 (sut/count-digits sample)))
  (is (= 274 (sut/count-digits input))))


(def sample-wire
  ["acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf"])

(deftest part-2
  (is (= 5353 (sut/match-wires
               (first
                (sut/load-wires sample-wire)))))

  (is (= 61229 (sut/add-wires sample)))
  (is (= 1012089 (sut/add-wires input))))






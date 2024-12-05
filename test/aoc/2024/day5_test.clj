(ns aoc.2024.day5-test
  (:require [aoc.2024.day5 :as sut]
            [clojure.string :as s]
            [clojure.test :as t]))

(defn parse-int [s]
  (Integer/parseInt s))


(defn- parse-ordering [input]
  (->> input
       (re-matches #"(\d+)\|(\d+)")
       rest
       (map parse-int)
       vec))


(defn parse-printing [input]
  (->> input
       (re-seq #"\d+")
       (map parse-int)
       vec))


(defn- parse-instructions [input]
  (let [[ordering printing] (split-with seq input)
        printing (rest printing)]
    [(map parse-ordering ordering)
     (map parse-printing printing)]))


(def input
  (->> "resources/2024/day5.txt"
       slurp
       s/split-lines
       parse-instructions))


(def sample
  (->> ["47|53"
        "97|13"
        "97|61"
        "97|47"
        "75|29"
        "61|13"
        "75|53"
        "29|13"
        "97|29"
        "53|29"
        "61|53"
        "97|53"
        "61|29"
        "47|13"
        "75|47"
        "97|75"
        "47|61"
        "75|61"
        "47|29"
        "75|13"
        "53|13"
        ""
        "75,47,61,53,29"
        "97,61,53,29,13"
        "75,29,13"
        "75,97,47,61,53"
        "61,13,29"
        "97,13,75,29,47"]
       parse-instructions))


(t/deftest part-1
  (t/is (= 143 (sut/correct-order-middle-sum sample)))
  (t/is (= 5329 (sut/correct-order-middle-sum input))))


(t/deftest part-2
  (t/is (= 123 (sut/incorrect-order-middle-sum sample)))
  (t/is (= 5833 (sut/incorrect-order-middle-sum input))))


(ns aoc.2022.day11-test
  (:require [aoc.2022.day11 :as sut]
            [clojure.string :as s]
            [clojure.test :refer [deftest is are] :as t]))

(defn read-monkeys
  [input]
  (->> input
       (partition-by empty?)
       (remove (comp empty? first))))

(def input
  (->> "resources/2022/day11.txt"
       slurp
       s/split-lines
       read-monkeys))

(def sample
  (->> ["Monkey 0:"
        "  Starting items: 79, 98"
        "  Operation: new = old * 19"
        "  Test: divisible by 23"
        "    If true: throw to monkey 2"
        "    If false: throw to monkey 3"
        ""
        "Monkey 1:"
        "  Starting items: 54, 65, 75, 74"
        "  Operation: new = old + 6"
        "  Test: divisible by 19"
        "    If true: throw to monkey 2"
        "    If false: throw to monkey 0"
        ""
        "Monkey 2:"
        "  Starting items: 79, 60, 97"
        "  Operation: new = old * old"
        "  Test: divisible by 13"
        "    If true: throw to monkey 1"
        "    If false: throw to monkey 3"
        ""
        "Monkey 3:"
        "  Starting items: 74"
        "  Operation: new = old + 3"
        "  Test: divisible by 17"
        "    If true: throw to monkey 0"
        "    If false: throw to monkey 1"]
       read-monkeys))

(defn equal-monkeys [{op1 :op :as m1} {op2 :op :as m2}]
  (and
   (= (select-keys m1 [:items :test])
      (select-keys m2 [:items :test]))
   (= (op1 5) (op2 5))))


(def sample-parsed
  [{:items [79 98] :op (partial * 19) :test [23 2 3]}
   {:items [54, 65, 75, 74] :op (partial + 6) :test [19 2 0]}
   {:items [79, 60, 97] :op #(* % %) :test [13 1 3]}
   {:items [74] :op (partial + 3) :test [17 0 1]}])

(deftest parse-monkey-test
  (are [idx] (equal-monkeys (sample-parsed idx) (sut/parse-monkey (nth sample idx)))
    0
    1
    2
    3))

(deftest keep-away-round-test
  (are [expected mks] (map :items (sut/keep-away-round mks))
    [[2]] [{:items [5] :op inc :test [2 0 1]}]
    [[20, 23, 27, 26]
     [2080, 25, 167, 207, 401, 1046]
     []
     []] sample-parsed))


(deftest part-1
  (is (= 10605 (sut/busiest-mb-a-bit-worried sample)))
  (is (= 110888 (sut/busiest-mb-a-bit-worried input))))


(deftest part-2
  (is (= 2713310158 (sut/busiest-mb-worried sample)))
  (is (= 25590400731 (sut/busiest-mb-worried input)))))

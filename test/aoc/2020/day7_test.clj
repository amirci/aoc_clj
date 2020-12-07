(ns aoc.2020.day7-test
  (:require [aoc.2020.day7 :as sut]
            [clojure.java.io :as io]
            [clojure.test :as t :refer :all]))

(def input
  (->> "2020/day7.input.txt"
       io/resource
       slurp
       clojure.string/split-lines))


(def sample-a
  ["wavy teal bags contain no other bags."
   "pale black bags contain 5 dark salmon bags."
   "clear gold bags contain 2 plaid white bags, 5 drab coral bags, 5 pale coral bags."
   "muted chartreuse bags contain 5 faded crimson bags."
   "dotted fuchsia bags contain 1 plaid brown bag, 1 dark violet bag."])

(deftest part-a
  (testing "parsing bag rule"
    (is (= ["wavy teal" []] (sut/parse-rule (first sample-a))))
    (is (= ["pale black" [{:total 5 :color "dark salmon"}]] (sut/parse-rule (second sample-a)))) )

  (testing "input"
    (is (= 197 (sut/bags-eventually "shiny gold" input)))))


(def sample-b
  ["shiny gold bags contain 2 dark red bags."
   "dark red bags contain 2 dark orange bags."
   "dark orange bags contain 2 dark yellow bags."
   "dark yellow bags contain 2 dark green bags."
   "dark green bags contain 2 dark blue bags."
   "dark blue bags contain 2 dark violet bags."
   "dark violet bags contain no other bags."])

(deftest part-b
  (testing "sample"
    (is (= 126 (sut/bags-total "shiny gold" sample-b))))
  (testing "input"
    (is (= 85324 (sut/bags-total "shiny gold" input)))))


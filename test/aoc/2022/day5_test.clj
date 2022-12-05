(ns aoc.2022.day5-test
  (:require [aoc.2022.day5 :as sut]
            [clojure.string :as s]
            [clojure.test :refer [deftest is] :as t]))

(def stacks
  (->> "resources/2022/day5.txt"
       slurp
       s/split-lines
       sut/read-stacks))

(def sample
  (->> ["    [D]    ",
        "[N] [C]    ",
        "[Z] [M] [P]",
        " 1   2   3 ",
        "",
        "move 1 from 2 to 1"
        "move 3 from 1 to 3"
        "move 2 from 2 to 1"
        "move 1 from 1 to 2"]
       sut/read-stacks))

(deftest move-crates
  (is (= [[\Z \P \M] []]
         (sut/move-crates 2 1 0 sut/default-crane [[\Z] [\M \P]])))
  (is (= [[\Z \N \D] [\M \C]]
         (sut/move-crates 1 1 0 sut/default-crane [[\Z \N] [\M \C \D]]))))

(deftest parse-instruction
  (is (= [[\Z \N \D] [\M \C]]
         ((sut/parse-instruction "move 1 from 2 to 1")
          sut/default-crane
          [[\Z \N] [\M \C \D]]))))

(deftest part-1
  (is (= [\C \M \Z] (sut/find-top-stacks sample)))
  (is (= [\J \D \T \M \R \W \C \Q \J] (sut/find-top-stacks stacks))))

(deftest part-2
  (is (= [\M \C \D] (sut/find-top-stacks-9001 sample)))
  (is (= [\V \H \J \D \D \C \W \R \D] (sut/find-top-stacks-9001 stacks))))

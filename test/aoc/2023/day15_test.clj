(ns aoc.2023.day15-test
  (:require [aoc.2023.day15 :as sut]
            [clojure.string :as s]
            [clojure.test :refer [deftest is] :as t]))

(def input
  (->> "resources/2023/day15.txt"
       slurp
       s/split-lines
       first))

(def sample
  "rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7")

(def expected-hashes
  [30 253 97 47 14 180 9 197 48 214 231])

(deftest part1-test
  (is (= 1320 (sut/sum-hash-values sample)))
  (is (= 494980 (sut/sum-hash-values input))))



(deftest part2-test
  (is (= 145 (sut/focusing-power sample)))
  (is (= 247933 (sut/focusing-power input))))






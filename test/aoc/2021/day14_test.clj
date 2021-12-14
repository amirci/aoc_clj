(ns aoc.2021.day14-test
  (:require [aoc.2021.day14 :as sut]
            [clojure.string :as st]
            [clojure.test :as t :refer [deftest is]]))

(def input
  (-> "resources/2021/day14.txt"
      slurp
      st/split-lines))

(def sample
  ["NNCB"
   ""
   "CH -> B"
   "HH -> N"
   "CB -> H"
   "NH -> C"
   "HB -> C"
   "HC -> B"
   "HN -> C"
   "NN -> C"
   "BH -> H"
   "NC -> B"
   "NB -> B"
   "BN -> B"
   "BB -> N"
   "BC -> B"
   "CC -> N"
   "CN -> C"])


(deftest part-1
  (is (= (frequencies "NBCCNBBBCBHCB") (sut/poly-steps 2 sample)))
  (is (= (frequencies "NBBBCNCCNBBNBNBBCHBHHBCHB") (sut/poly-steps 3 sample)))
  (is (= (frequencies "NBBNBNBBCCNBCNCCNBBNBBNBBBNBBNBBCBHCBHHNHCBBCBHCB") (sut/poly-steps 4 sample)))
  (is (= 1588 (sut/subtract-poly 10 sample)))
  (is (= 2027 (sut/subtract-poly 10 input))))


(deftest part-2
  (is (= 2188189693529 (sut/subtract-poly 40 sample)))
  (is (= 2265039461737 (sut/subtract-poly 40 input))))


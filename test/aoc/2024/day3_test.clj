(ns aoc.2024.day3-test
  (:require [aoc.2024.day3 :as sut]
            [clojure.test :as t]))


(def sample
  "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))")


(def input
  (->> "resources/2024/day3.txt"
       slurp))

(t/deftest part-1
  (t/is (= 161 (sut/add-multiplications sample)))
  (t/is (= 167650499  (sut/add-multiplications input))))


(def sample2
  "xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))")


(t/deftest part-2
  (t/is (= 48 (sut/add-enabled-multiplications sample2)))
  (t/is (= 95846796 (sut/add-enabled-multiplications input))))

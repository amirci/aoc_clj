(ns aoc.2024.day9-test
  (:require [aoc.2024.day9 :as sut]
            [clojure.test :as t]
            [clojure.string :as s]))

(def parse-int #(Character/digit % 10))

(def sample
  (->> "2333133121414131402"
       (map parse-int)))

(def input
  (->> "resources/2024/day9.txt"
       slurp
       s/split-lines
       first
       (map parse-int)))


(t/deftest part-1
  (t/is (= 1928 (sut/compact-disk sample)))
  (t/is (= 6291146824486 (sut/compact-disk input))))


(t/deftest part-2
  (t/is (= 2858 (sut/compact-disk-full-blocks sample)))
  (t/is (= 6307279963620 (sut/compact-disk-full-blocks input))))


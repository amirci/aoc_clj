(ns aoc.2025.day02-test
  (:require [aoc.2025.day02 :as sut]
            [clojure.string :as s]
            [clojure.test :as t]))


(defn- create-ranges [line]
  (->> (s/split line #",")
       (map #(s/split % #"-"))
       (map (partial map read-string))
       (map (fn [[start end]] (range start (inc end))))))


(def sample-ranges
  (->> "11-22,95-115,998-1012,1188511880-1188511890,222220-222224,1698522-1698528,446443-446449,38593856-38593862,565653-565659,824824821-824824827,2121212118-2121212124"
       create-ranges))


(def input-ranges
  (->> "resources/2025/day_02.txt"
       slurp
       s/split-lines
       first
       (create-ranges)))


(t/deftest part-1
  (t/is (= 1227775554 (sut/sum-invalid-ids sample-ranges)))
  (t/is (= 19219508902 (sut/sum-invalid-ids input-ranges))))


(t/deftest part-2
  (t/is (= 4174379265 (sut/sum-invalid-ids-2 sample-ranges)))
  (t/is (= 27180728081 (sut/sum-invalid-ids-2 input-ranges))))


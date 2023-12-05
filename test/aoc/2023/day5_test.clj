(ns aoc.2023.day5-test
  (:require [aoc.2023.day5 :as sut]
            [clojure.string :as s]
            [clojure.test :refer [deftest is] :as t]))


(def almanac
  (->> "resources/2023/day5.txt"
       slurp
       s/split-lines))

(def almanac-sample
  ["seeds: 79 14 55 13"
   ""
   "seed-to-soil map:"
   "50 98 2"
   "52 50 48"
   ""
   "soil-to-fertilizer map:"
   "0 15 37"
   "37 52 2"
   "39 0 15"
   ""
   "fertilizer-to-water map:"
   "49 53 8"
   "0 11 42"
   "42 0 7"
   "57 7 4"
   ""
   "water-to-light map:"
   "88 18 7"
   "18 25 70"
   ""
   "light-to-temperature map:"
   "45 77 23"
   "81 45 19"
   "68 64 13"
   ""
   "temperature-to-humidity map:"
   "0 69 1"
   "1 0 69"
   ""
   "humidity-to-location map:"
   "60 56 37"
   "56 93 4"])

(deftest part-1
  (is (= 35 (sut/lowest-seed-location-single almanac-sample)))
  (is (= 1181555926 (sut/lowest-seed-location-single almanac))))


(deftest part-2
  (is (= 46 (sut/lowest-seed-location-from-ranges almanac-sample)))
  (is (= 37806486 (sut/lowest-seed-location-from-ranges almanac))))

(deftest overlap-ranges-test
  (t/are [rng match target expected] (= (set expected ) (set (sut/overlap-ranges rng [match target]) ))
    [1 5] [1 5] [2 6] [[2 6]]
    [1 5] [2 5] [3 6] [[1 1] [3 6]]
    [2 7] [2 5] [3 6] [[3 6] [6 7]]))

(def alma
  (sut/create-almanac almanac-sample))


; 82, which corresponds to soil 84, fertilizer 84, water 84, light 77, temperature 45, humidity 46, and location 46. So, the lowest location number is 46.
(deftest follow-ranges-test
  (t/are [k rngs exp-k exp-rngs] (= [exp-k exp-rngs] (sut/follow-ranges alma [k rngs]))
    "seed" [[79 92]] "soil" [[81 94]]
    "soil" [[81 94]] "fertilizer" [[81 94]]
    "fertilizer" [[81 94]] "water" [[81 94]]
    "water" [[81 94]] "light" [[74 87]]
    "light" [[74 87]] "temperature" [[45 55] [78 80]]
    "temperature" [[78 80] [45 55]] "humidity" [[46 56] [78 80] ]
    "humidity" [[78 80] [46 56]] "location" [[46 55] [60 60] [82 84]]
    ))



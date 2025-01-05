(ns aoc.2024.day7-test
  (:require [aoc.2024.day7 :as sut]
            [clojure.string :as s]
            [clojure.test :as t]))


(defn- parse-line [line]
  (->> line
       (re-seq #"\d+")
       (map bigint))
  )

(def sample
  (->> ["190: 10 19"
        "3267: 81 40 27"
        "83: 17 5"
        "156: 15 6"
        "7290: 6 8 6 15"
        "161011: 16 10 13"
        "192: 17 8 14"
        "21037: 9 7 18 13"
        "292: 11 6 16 20"]
       (map parse-line)))


(def input
  (->> "resources/2024/day7.txt"
       slurp
       s/split-lines
       (map parse-line)))


(t/deftest part-1
  (t/is (= 3749 (sut/total-calibration sample)))
  (t/is (= 1708857123053N (sut/total-calibration input))))


(t/deftest part-2
  (t/is (= 11387 (sut/total-calibration-with-concat sample)))
  (t/is (= 189207836795655N  (sut/total-calibration-with-concat input))))

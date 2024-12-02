(ns aoc.2024.day2-test
  (:require [aoc.2024.day2 :as sut]
            [clojure.string :as s]
            [clojure.test :as t]))

(def parse-int #(Integer/parseInt %))


(defn lines->reports [lines]
  (->> lines
       (map #(s/split % #"\s+"))
       (map (partial map parse-int))
       (map vec)))

(def reports
  (->> "resources/2024/day2.txt"
       slurp
       s/split-lines
       lines->reports))


(def sample
  (->> ["7 6 4 2 1"
        "1 2 7 8 9"
        "9 7 6 2 1"
        "1 3 2 4 5"
        "8 6 4 4 1"
        "1 3 6 7 9"]
       lines->reports))


(t/deftest part-1
  (t/is (= 4 (sut/total-tolerate-reports sample)))
  (t/is (= 566 (sut/total-tolerate-reports reports))))



(ns aoc.2025.day03-test
  (:require [aoc.2025.day03 :as sut]
            [clojure.string :as s]
            [clojure.test :as t]))

(defn char->digit [c] (Character/digit c 10))

(def line->digits (partial map char->digit))

(def battery-sample
  (->> ["987654321111111"
        "811111111111119"
        "234234234234278"
        "818181911112111"]
       (map line->digits)))


(def battery-input
  (->> "resources/2025/day_03.txt"
       slurp
       s/split-lines
       (map line->digits)))


(t/deftest part-1
  (t/is (= 357 (sut/total-joltage battery-sample)))
  (t/is (= 17346 (sut/total-joltage battery-input))))

(t/deftest part-2
  (t/is (= 3121910778619 (sut/total-joltage-12 battery-sample)))
  (t/is (= 172981362045136 (sut/total-joltage-12 battery-input))))


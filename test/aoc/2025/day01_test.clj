(ns aoc.2025.day01-test
  (:require [aoc.2025.day01 :as sut]
            [clojure.string :as s]
            [clojure.test :refer [deftest is] :as t]))


(def parse-int #(Integer/parseInt %))

(defn- parse-instruction [[dir & nbr]]
  [dir (parse-int (apply str nbr))])

(def instructions
  (->> "resources/2025/day_01.txt"
       slurp
       s/split-lines
       (map parse-instruction)))


(def sample-instructions
  (->> ["L68"
        "L30"
        "R48"
        "L5"
        "R60"
        "L55"
        "L1"
        "L99"
        "R14"
        "L82"]
       (map parse-instruction)))

(deftest part-1
  (is (= 3 (sut/calc-password sample-instructions)))
  (is (= 984 (sut/calc-password instructions))))


(deftest times-crossing-test
  (is (= [20 3] (sut/times-crossing-zero [20] [\R 300])))
  (is (= [10 1] (sut/times-crossing-zero [20] [\R 90])))
  (is (= [30 1] (sut/times-crossing-zero [20] [\L 90])))
  (is (= [0  2] (sut/times-crossing-zero [0] [\L 200])))
  (is (= [0  1] (sut/times-crossing-zero [52] [\R 48])))
  (is (= [95 0] (sut/times-crossing-zero [0] [\L 5])))
  (is (= [20 3] (sut/times-crossing-zero [20] [\L 300]))))

(deftest part-2
  (is (= 6 (sut/calc-password-0x434C49434B sample-instructions)))
  (is (= 5657 (sut/calc-password-0x434C49434B instructions))))


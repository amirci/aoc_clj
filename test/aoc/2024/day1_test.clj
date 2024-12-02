(ns aoc.2024.day1-test
  (:require [aoc.2024.day1 :as sut]
            [clojure.string :as s]
            [clojure.test :refer [deftest is] :as t]))


(def parse-int #(Integer/parseInt %))


(defn lines->offices [lines]
  (->> lines
       (map #(s/split % #"\s+"))
       (map (partial map parse-int))
       (apply mapv vector)))

(def offices
  (->> "resources/2024/day1.txt"
       slurp
       s/split-lines
       lines->offices))

(def sample
  (->> ["3   4"
        "4   3"
        "2   5"
        "1   3"
        "3   9"
        "3   3"]
       lines->offices))

(deftest part-1
  (is (= 11 (sut/total-distances sample)))
  (is (= 3246517 (sut/total-distances offices))))


(t/deftest part-2
  (t/is (= 31 (sut/similarity-score sample)))
  (t/is (= 29379307 (sut/similarity-score offices))))

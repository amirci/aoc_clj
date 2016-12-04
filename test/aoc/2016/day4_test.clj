(ns aoc.2016.day4-test
  (:require [clojure.test :refer :all]
            [aoc.2016.day4 :refer :all]))


(def rooms
  (let [to-int #(Integer. %)
        lines (-> "resources/2016/day4.input.txt" slurp clojure.string/split-lines)]
    (->> lines
         (map #(clojure.string/replace % "-" ""))
         (map #(drop 1 (re-matches #"([a-z]+)(\d+)\[([a-z]+)\]" %))))))


(deftest part-a
  (is (= 158835 (sum-sectors rooms))))


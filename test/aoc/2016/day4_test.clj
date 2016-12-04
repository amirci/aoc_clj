(ns aoc.2016.day4-test
  (:require [clojure.test :refer :all]
            [aoc.2016.day4 :refer :all]))


(def rooms
  (let [lines (-> "resources/2016/day4.input.txt" slurp clojure.string/split-lines)]
    (->> lines
         (map #(clojure.string/replace % "-" ""))
         (map #(drop 1 (re-matches #"([a-z]+)(\d+)\[([a-z]+)\]" %))))))

(def rooms2
  (let [lines (-> "resources/2016/day4.input.txt" slurp clojure.string/split-lines)]
    (->> lines
         (map #(drop 1 (re-matches #"([^\d]+)(\d+)\[[a-z]+\]" %))))))

; rank 317
(deftest part-a
  (is (= 158835 (sum-sectors rooms))))

(deftest decrypt-sample
  (is (= "very encrypted name " (decrypt ["qzmt-zixmtkozy-ivhz-" "343"]))))

; rank 511
(deftest part-b
  (is (= "993" (sector-for "northpole object storage " rooms2))))

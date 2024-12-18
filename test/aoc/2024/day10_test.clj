(ns aoc.2024.day10-test
  (:require [aoc.2024.day10 :as sut10]
            [clojure.string :as s]
            [clojure.test :as t]))

(def digit->int #(Character/digit % 10))


(defn ->topo-map [lines]
  (let [rows (count lines)
        cols (count (first lines))]
    (into {}
          (for [x (range rows) y (range cols)]
            [[x y] (digit->int (get-in lines [x y]))]))))


(def input
  (->> "resources/2024/day10.txt"
       slurp
       s/split-lines
       ->topo-map))


(def sample
  (->> ["89010123"
        "78121874"
        "87430965"
        "96549874"
        "45678903"
        "32019012"
        "01329801"
        "10456732"]
       ->topo-map))


(t/deftest part-1
  (t/is (= 36 (sut10/sum-scores-trailheads sample)))
  (t/is (= 789 (sut10/sum-scores-trailheads input))))


(t/deftest part-2
  (t/is (= 81 (sut10/sum-rating-trailheads sample)))
  (t/is (= 1735 (sut10/sum-rating-trailheads input))))

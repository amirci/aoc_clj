(ns aoc.2024.day12-test
  (:require [aoc.2024.day12 :as sut]
            [clojure.set :as cset]
            [clojure.string :as s]
            [clojure.test :as t]))

(defn- ->region-map [lines]
  (let [rows (count lines)
        cols (count (first lines))]
    (into {}
          (for [x (range rows) y (range cols)]
            [[x y] (get-in lines [x y])]))))

(def sample
  (->> ["RRRRIICCFF"
        "RRRRIICCCF"
        "VVRRRCCFFF"
        "VVRCCCJFFF"
        "VVVVCJJCFE"
        "VVIVCCJJEE"
        "VVIIICJJEE"
        "MIIIIIJJEE"
        "MIIISIJEEE"
        "MMMISSJEEE"]
       ->region-map))

(def input
  (->> "resources/2024/day12.txt"
       slurp
       s/split-lines
       ->region-map))


(t/deftest part-1
  (t/is (= 1930 (sut/total-price sample)))
  (t/is (= 1494342 (sut/total-price input))))


(def sample2
  (->> ["AAAA"
        "BBCD"
        "BBCC"
        "EEEC"]
       ->region-map))

(def sample3
  (->> ["EEEEE"
        "EXXXX"
        "EEEEE"
        "EXXXX"
        "EEEEE"]
       ->region-map))


(def sample5
  (->> ["EEEEEE"
        "EXXXXE"
        "EEEEEE"
        "EXXXXE"
        "EEEEEE"]
       ->region-map))


(def sample6
  (->> ["OOOOO"
        "OXOXO"
        "OOOOO"
        "OXOXO"
        "OOOOO"]
       ->region-map))

(t/deftest part-2
  (t/is (= 80 (sut/total-price-bulk sample2)))
  (t/is (= 236 (sut/total-price-bulk sample3)))
  (t/is (= 1206 (sut/total-price-bulk sample)))
  (t/is (= 1206 (sut/total-price-bulk sample)))
  (t/is (= 436 (sut/total-price-bulk sample6))) ;; 4 + 4 + 4 + 4 + 21 * 20
  (t/is (= 296 (sut/total-price-bulk sample5))) ;; 4 * 4 + 4 * 4 + 19 * 12
  (t/is (= 893676 (sut/total-price-bulk input))))


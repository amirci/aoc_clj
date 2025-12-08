(ns aoc.2025.day07-test
  (:require [aoc.2025.day07 :as sut]
            [clojure.test :as t]
            [clojure.string :as s]))


(defn- splitters-positions [[row line]]
  (->> line
       (map-indexed vector)
       (remove (comp #{\. \S} second))
       (map (fn [[col]] [row col]))))


(defn- parse-splitters [lines]
  (->> lines
       (map-indexed vector)
       (mapcat splitters-positions)
       set
       (assoc {:start (vector 0 (s/index-of (first lines) \S))}
              :length (count lines)
              :width (count (first lines))
              :splitters)))


(def manifold-sample
  (->> [".......S......."
        "..............."
        ".......^......."
        "..............."
        "......^.^......"
        "..............."
        ".....^.^.^....."
        "..............."
        "....^.^...^...."
        "..............."
        "...^.^...^.^..."
        "..............."
        "..^...^.....^.."
        "..............."
        ".^.^.^.^.^...^."
        "..............."]
       parse-splitters))


(def manifold-input
  (->> "resources/2025/day_07.txt"
       slurp
       s/split-lines
       parse-splitters))


(t/deftest part-1
  (t/is (= 21 (sut/count-beam-split manifold-sample)))
  (t/is (= 1570 (sut/count-beam-split manifold-input))))



(t/deftest part-2
  (t/is (= 40 (sut/count-beam-paths manifold-sample)))
  (t/is (= 15118009521693 (sut/count-beam-paths manifold-input))))

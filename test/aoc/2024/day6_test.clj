(ns aoc.2024.day6-test
  (:require [aoc.2024.day6 :as sut]
            [clojure.string :as s]
            [clojure.test :as t]))


(def input
  (->> "resources/2024/day6.txt"
       slurp
       s/split-lines))


(def sample
  ["....#....."
   ".........#"
   ".........."
   "..#......."
   ".......#.."
   ".........."
   ".#..^....."
   "........#."
   "#........."
   "......#..."])


(t/deftest part-1
  (t/is (= 41 (sut/guard-steps sample)))
  (t/is (= 4374 (sut/guard-steps input))))


(t/deftest part-2
  (t/is (= 6 (sut/guard-loops sample)))
  ; 1776 too high
  ; 1778 when adding obstacle instead of rotating first
  ; 1 when switching to visited first
  ; updating the obstacles on the fly did not work
  ; running it once and then mapping over each obstacle did work
  (t/is (= 1705 (sut/guard-loops input))))


(defn generate-map [{:keys [visited obstacles rows cols start]}]
  (let [visited (set (map first visited))
        obs (first obstacles)]
    (for [row (range rows)]
      (apply str
             (for [col (range cols)]
               (cond
                 (obstacles [row col]) (if (= obs [row col]) \O \#)
                 (visited [row col]) (if (= start [row col]) \S \X)
                 :else \.))))))


(defn write-loop [[[x y] output]]
  (->> output
       (s/join "\n")
       (spit (format "/tmp/loop-%d-%d.txt" x y))))



(ns aoc.2023.day11-test
  (:require [aoc.2023.day11 :as sut]
            [clojure.string :as s]
            [clojure.test :refer [deftest is are] :as t]))


(def input
  (->> "resources/2023/day11.txt"
       slurp
       s/split-lines))


(def sample
  ["...#......"
   ".......#.."
   "#........."
   ".........."
   "......#..."
   ".#........"
   ".........#"
   ".........."
   ".......#.."
   "#...#....."])

(deftest distance-test
  (are [g1 g2 expected] (= expected (sut/distance g1 g2))
    [10 9] [0 4] 15
    [7 12] [2 0] 17
    [11 0] [11 5] 5))

(deftest part-1
  (is (= 374 (sut/shortest-paths 2 sample)))
  (is (= 9312968 (sut/shortest-paths 2 input))))


(deftest part-2
  (is (= 1030 (sut/shortest-paths 10 sample)))
  (is (= 8410 (sut/shortest-paths 100 sample)))
  (is (= 597714117556 (sut/shortest-paths 1000000 input))))

(ns aoc.2023.day13-test
  (:require [aoc.2023.day13 :as sut]
            [clojure.string :as s]
            [clojure.test :refer [deftest is] :as t]))

(def input
  (->> "resources/2023/day13.txt"
       slurp
       s/split-lines
       (map vec)
       (partition-by empty?)
       (remove (comp empty? first))
       (map vec)))


(def sample-column
  ["#.##..##."
   "..#.##.#."
   "##......#"
   "##......#"
   "..#.##.#."
   "..##..##."
   "#.#.##.#."])

(def sample-row
  ["#...##..#"
   "#....#..#"
   "..##..###"
   "#####.##."
   "#####.##."
   "..##..###"
   "#....#..#"])

(def sample-2
  ["##..#.#..##"
   "###...#...."
   "##.##.##..."
   "....###..##"
   "####..###.."
   "##.####...."
   "...####.###"
   "###...##..."
   "##...#..###"
   "##...#....."
   ".##..##.###"])


(def sample-3
  ["###.##.###."
   "###.##.####"
   ".#..#..#..#"
   "..#.###...."
   "#.##.##...."
   "...#.##.###"
   "########..#"
   "..#.#..#..."
   "....#.#.#.#"
   "#..#..#.#.#"
   "#..#..#.#.#"])

(deftest part1-test
  (is (= 5 (sut/find-mirror-line sample-column)))
  (is (= 400 (sut/find-mirror-line sample-row)))
  (is (= 33735 (sut/sum-mirrors input))))


(deftest part2-test
  (is (= 300 (sut/fix-smudge (vec (map vec sample-column)))))
  (is (= 100 (sut/fix-smudge (vec (map vec sample-row)))))
  (is (= 38063 (sut/sum-mirrors-fixing-smudges input))))



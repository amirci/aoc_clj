(ns aoc.2023.day14-test
  (:require [aoc.2023.day14 :as sut]
            [clojure.string :as s]
            [clojure.test :refer [deftest is are] :as t]))


(def input
  (->> "resources/2023/day14.txt"
       slurp
       s/split-lines
       (mapv vec)))


(def sample
  (->> ["O....#...."
        "O.OO#....#"
        ".....##..."
        "OO.#O....O"
        ".O.....O#."
        "O.#..O.#.#"
        "..O..#O..O"
        ".......O.."
        "#....###.."
        "#OO..#...."]
       (mapv vec)))

(def expected-tilt
  (->> ["OOOO.#.O.."
        "OO..#....#"
        "OO..O##..O"
        "O..#.OO..."
        "........#."
        "..#....#.#"
        "..O..#.O.O"
        "..O......."
        "#....###.."
        "#....#...."]
       (mapv vec)))


(deftest move-rocks-front-test
  (are [src expected] (= (vec expected)
                         (sut/move-rocks-front (vec src)))
    "OO.O.O..##" "OOOO....##"
    "...OO....O" "OOO......."
    ".#.O.#O..." ".#O..#O..."))


(deftest tilt-north-test
  (is (= expected-tilt (sut/tilt-north sample))))


(deftest part1-test
  (is (= 136 (sut/sum-tilted-rocks sample)))
  (is (= 107951 (sut/sum-tilted-rocks input))))

(deftest tilt-west-test
  (is (= expected-tilt (sut/tilt-north sample))))


(def one-cycle
  [".....#...."
   "....#...O#"
   "...OO##..."
   ".OO#......"
   ".....OOO#."
   ".O#...O#.#"
   "....O#...."
   "......OOOO"
   "#...O###.."
   "#..OO#...."])

(def two-cycles
  [".....#...."
   "....#...O#"
   ".....##..."
   "..O#......"
   ".....OOO#."
   ".O#...O#.#"
   "....O#...O"
   ".......OOO"
   "#..OO###.."
   "#.OOO#...O"])

(def three-cycles
  [".....#...."
   "....#...O#"
   ".....##..."
   "..O#......"
   ".....OOO#."
   ".O#...O#.#"
   "....O#...O"
   ".......OOO"
   "#...O###.O"
   "#.OOO#...O"])

(deftest cycle-test
  (are [n expected] (= expected (->> sample
                                     (sut/cycle-for n)
                                     (map #(apply str %))))
    1 one-cycle
    2 two-cycles
    3 three-cycles))

(deftest part2-test
  (is (= 64 (sut/sum-tilted-cycle 1000000000 sample)))
  (is (= 95736 (sut/sum-tilted-cycle 1000000000 input))))


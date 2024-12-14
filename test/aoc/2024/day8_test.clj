(ns aoc.2024.day8-test
  (:require [aoc.2024.day8 :as sut]
            [clojure.string :as s]
            [clojure.test :as t]))


(defn parse-antennas [lines]
  (let [rows (count lines) 
        cols (count (first lines))]
    (->> (for [row (range rows) col (range cols)] [row col])
         (map (fn [pos] [(get-in lines pos) pos]))
         (remove (comp (partial = \.) first))
         sut/multiset
         (vector rows cols))))


(def sample
  (->> ["............"
        "........0..."
        ".....0......"
        ".......0...."
        "....0......."
        "......A....."
        "............"
        "............"
        "........A..."
        ".........A.."
        "............"
        "............"]
       parse-antennas))


(t/deftest antinodes-test
  (t/is (= [[7 6] [1 3]] (sut/antinodes [[3 4] [5 5]])))
  (t/is (= [[6 2] [3 11]] (sut/antinodes [[4 8] [5 5]]))))

(def input
  (->> "resources/2024/day8.txt"
       slurp
       s/split-lines
       parse-antennas))

(t/deftest part-1
  (t/is (= 14 (sut/total-antinodes sample)))
  (t/is (= 252 (sut/total-antinodes input))))


(def expected-t-for-sample
  ["##....#....#"
   ".#.#....0..."
   "..#.#0....#."
   "..##...0...."
   "....0....#.."
   ".#...#A....#"
   "...#..#....."
   "#....#.#...."
   "..#.....A..."
   "....#....A.."
   ".#........#."
   "...#......##"])


(t/deftest part-2
  (t/is (= 34 (sut/total-t-antinodes sample)))
  (t/is (= 839 (sut/total-t-antinodes input))))


(defn- create-map [rows cols pts]
  (->> (for [row (range rows)]
         (for [col (range cols)]
           (if (pts [row col]) \# \.)))))


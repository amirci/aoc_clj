(ns aoc.2024.day4-test
  (:require [aoc.2024.day4 :as sut]
            [clojure.string :as s]
            [clojure.test :as t]))

(def input
  (->> "resources/2024/day4.txt"
       slurp
       s/split-lines))

(def sample
  ["MMMSXXMASM"
   "MSAMXMSMSA"
   "AMXSXMAAMM"
   "MSAMASMSMX"
   "XMASAMXAMM"
   "XXAMMXXAMA"
   "SMSMSASXSS"
   "SAXAMASAAA"
   "MAMMMXMMMM"
   "MXMXAXMASX "])


(def solution
  ["....XXMAS."
   ".SAMXMS..."
   "...S..A..."
   "..A.A.MS.X"
   "XMASAMX.MM"
   "X.....XA.A"
   "S.S.S.S.SS"
   ".A.A.A.A.A"
   "..M.M.M.MM"
   ".X.X.XMASX"])

(t/deftest part-1
  (t/is (= 18 (sut/find-xmas sample)))
  (t/is (= 2468 (sut/find-xmas input))))


(def solution2
  [".M.S......"
   "..A..MSMS."
   ".M.S.MAA.."
   "..A.ASMSM."
   ".M.S.M...."
   ".........."
   "S.S.S.S.S."
   ".A.A.A.A.."
   "M.M.M.M.M."
   ".........."])


(t/deftest part-2
  (t/is (= 9 (sut/find-x-mas sample)))
  (t/is (= 1864 (sut/find-x-mas input))))

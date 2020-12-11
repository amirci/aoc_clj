(ns aoc.2020.day11-test
  (:require [aoc.2020.day11 :as sut]
            [clojure.java.io :as io]
            [clojure.test :as t :refer :all]))

(def input
  (->> "2020/day11.input.txt"
       io/resource
       slurp
       clojure.string/split-lines))

(def sample-a
  ["L.LL.LL.LL"
   "LLLLLLL.LL"
   "L.L.L..L.."
   "LLLL.LL.LL"
   "L.LL.LL.LL"
   "L.LLLLL.LL"
   "..L.L....."
   "LLLLLLLLLL"
   "L.LLLLLL.L"
   "L.LLLLL.LL"])

(def sample-av
  (mapv vec sample-a))

(def sample-a-occupied
  ["#.##.##.##"
   "#######.##"
   "#.#.#..#.."
   "####.##.##"
   "#.##.##.##"
   "#.#####.##"
   "..#.#....."
   "##########"
   "#.######.#"
   "#.#####.##"])


(deftest part-a
  (testing "sample"
    (is (= (mapv vec sample-a-occupied)
           (:current (sut/seat-people {:fn sut/seating-adj :current sample-av}))))
    (is (= 37 (sut/seating-area sample-a))))

  (testing "input"
    (is (= 2194 (sut/seating-area input)))))


(def sample-b
  [".......#."
   "...#....."
   ".#......."
   "........."
   "..#L....#"
   "....#...."
   "........."
   "#........"
   "...#....."])

(def sample-empty
  [".##.##."
   "#.#.#.#"
   "##...##"
   "...L..."
   "##...##"
   "#.#.#.#"
   ".##.##."])


(deftest part-b
  (testing "sample"
    (is (= 0 (sut/first-see [3 3] (mapv vec sample-empty) )))
    (is (= 8 (sut/first-see [4 3] (mapv vec sample-b) )))
    (is (= 26 (sut/seating-area-wide sample-a))))

  (testing "input"
    (is (= 1944 (sut/seating-area-wide input)))))



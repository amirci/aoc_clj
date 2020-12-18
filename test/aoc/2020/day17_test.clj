(ns aoc.2020.day17-test
  (:require [aoc.2020.day17 :as sut]
            [clojure.test :as t :refer :all]))

(def input
  ["..##.##."
   "#.#..###"
   "##.#.#.#"
   "#.#.##.#"
   "###..#.."
   ".#.#..##"
   "#.##.###"
   "#.#..##."])


(def sample
  [".#."
   "..#"
   "###"])

(def sample-grid (sut/->grid sample))

(def zn1
  ["#.."
   "..#"
   ".#."])

(def z0
  ["#.#"
   ".##"
   ".#."])

(def z1
  ["#.."
   "..#"
   ".#."])

(defn expand-rows [grid z]
  (vector
   z
   (for [x (range 3)]
     (apply str
            (for [y (range 3)]
              (if (grid [z x y]) \# \.))))))

(defn grid->chars [grid]
  (->> grid
       (map last)
       (map (partial expand-rows grid))
       (into {})))


(deftest part-a
  (testing "sample"
    (is (= 112 (count (sut/boot-cycle sample)))))

  (testing "input"
    (is (= 424 (count (sut/boot-cycle input))))))


(deftest part-b
  (testing "sample"
    (is (= 848 (count (sut/boot-cycle4 sample)))))

  (testing "input"
    (is (= 2460 (count (sut/boot-cycle4 input))))))


(ns aoc.2016.day13-test
  (:require [clojure.test :refer :all]
            [aoc.2016.day13 :refer :all]))


(def expected-walls
  [".#.####.##"
   "..#..#...#"
   "#....##..."
   "###.#.###."
   ".##..#..#."
   "..##....#."
   "#...##.###"])


(deftest part-a-sample-test
  (doseq [x (range 7) y (range 10)]
    (let [es (nth (nth expected-walls x) y)
          es (if (= es \.) :space :wall)]
      (is (= es (calc-maze y x 10)) (str "position " x " " y)))))

(deftest part-a
  (is (= 90 (distance-to [1 1] [31 39] favorite-nbr))))

(deftest part-b
  (is (= 135 (all-locations [1 1] 50 favorite-nbr))))

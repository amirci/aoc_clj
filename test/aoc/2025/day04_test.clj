(ns aoc.2025.day04-test
  (:require [aoc.2025.day04 :as sut]
            [clojure.test :as t]
            [clojure.string :as s]))


(defn- parse-rolls-positions [lines]
  (let [lines (->> lines (map vec) vec)]
    (->> (for [x (range 0 (count lines))
               y (range 0 (count (first lines)))]
           [[x y] (get-in lines [x y])])
         (filter (comp (partial = \@) second))
         (map first)
         set)))


(def rolls-sample
  (->> ["..@@.@@@@."
        "@@@.@.@.@@"
        "@@@@@.@.@@"
        "@.@@@@..@."
        "@@.@@@@.@@"
        ".@@@@@@@.@"
        ".@.@.@.@@@"
        "@.@@@.@@@@"
        ".@@@@@@@@."
        "@.@.@@@.@."]
       parse-rolls-positions))

(def rolls-input
  (->> "resources/2025/day_04.txt"
       slurp
       s/split-lines
       parse-rolls-positions))


(t/deftest part-1
  (t/is (= 13 (sut/find-rolls-to-lift rolls-sample)))
  (t/is (= 1578 (sut/find-rolls-to-lift rolls-input))))


(t/deftest part-2
  (t/is (= 43 (sut/remove-all-possible-rolls rolls-sample)))
  (t/is (= 10132 (sut/remove-all-possible-rolls rolls-input))))



(ns aoc.2018.day18-test
  (:require [clojure.test :refer :all]
            [aoc.2018.day18 :as dut]))

(defn to-acres
  [lines]
  (->> lines
       (map-indexed vector)
       (reduce
         (fn [m [i line]]
           (->> line
                (map-indexed #(vector [i %1] %2))
                (into {})
                (merge m)))
         {})))

(def input
  (-> "resources/2018/day18.input.txt"
      slurp
      clojure.string/split-lines
      to-acres))

(def sample-input
  (-> [".#.#...|#."
       ".....#|##|"
       ".|..|...#."
       "..|#.....#"
       "#.#|||#|#|"
       "...#.||..."
       ".|....|..."
       "||...#|.#|"
       "|.||||..|."
       "...#.|..|."]
      to-acres))

(defn print-acres
  [[max-x max-y] acres]
  (doseq [y (range max-y)]
    (doseq [x (range max-x)]
      (print (acres [y x])))
    (println)))

(deftest part-a-test
  (is (= 507755 (dut/part-a 10 input))))

(deftest part-b-test
  (is (= 235080 (dut/part-b 1000000000 input))))




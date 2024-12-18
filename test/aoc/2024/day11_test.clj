(ns aoc.2024.day11-test
  (:require [aoc.2024.day11 :as sut]
            [clojure.test :as t]))


(def input [7568 155731 0 972 1 6919238 80646 22])

(def sample [125 17])

(t/deftest part-1
  (t/is (= [2097446912 14168 4048 2 0 2 4 40 48 2024 40 48 80 96 2 8 6 7 6 0 3 2]
           (sut/stones-tick 6 sample)))
  (t/is (= 22 (sut/count-stones-tick 6 sample)))
  (t/is (= 55312 (sut/count-stones-tick 25 sample)))
  (t/is (= 186424 (sut/count-stones-tick 25 input))))


(t/deftest part-2
  (t/is (= 219838428124832 (sut/count-stones-tick-o 75 input))))


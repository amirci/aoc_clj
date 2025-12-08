(ns aoc.2025.day06-test
  (:require [aoc.2025.day06 :as sut]
            [clojure.string :as s]
            [clojure.test :as t]))

(def sample-math-problems
  (->> [
"123 328  51 64 "
" 45 64  387 23 "
"  6 98  215 314"
"*   +   *   +  "
        ]))

(def math-problems
  (->> "resources/2025/day_06.txt"
       slurp
       s/split-lines))


(t/deftest part-1
  (t/is (= 4277556 (sut/solve-cephalopod-math sample-math-problems)))
  (t/is (= 4878670269096 (sut/solve-cephalopod-math math-problems))))


(t/deftest part-2
  (t/is (= 3263827 (sut/solve-cephalopod-math-correctly sample-math-problems)))
  (t/is (= 8674740488592 (sut/solve-cephalopod-math-correctly math-problems))))

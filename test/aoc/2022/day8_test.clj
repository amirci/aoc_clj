(ns aoc.2022.day8-test
  (:require [aoc.2022.day8 :as sut]
            [clojure.string :as s]
            [clojure.test :refer [deftest is are] :as t]))

(def input
  (->> "resources/2022/day8.txt"
       slurp
       s/split-lines
       (mapv #(mapv (comp read-string str) %))))

(def sample
  (->> [[3 0 3 7 3]
        [2 5 5 1 2]
        [6 5 3 3 2]
        [3 3 5 4 9]
        [3 5 3 9 0]]))


(deftest visible-dir-test
  (are [dir pos expected] (= expected (sut/visible-dir? sample pos dir))
    [0 1] [1 3] false
    [1 0] [1 3] false
    [-1 0] [1 3] false
    [0 -1] [1 3] false
    [0 -1] [1 1] true
    [-1 0] [1 1] true))

(deftest part-1
  (is (= 21 (sut/count-visible-trees sample)))
  (is (= 1763 (sut/count-visible-trees input))))

(deftest count-trees-test
  (are [pos dir expected] (= expected (sut/count-trees sample pos dir))
    [1 1] [-1 0] 1
    [1 1] [1 0] 1
    [2 1] [1 0] 2
    [3 2] [-1 0] 2
    [3 2] [1 0] 1
    [3 2] [0 -1] 2))

(deftest part-2
  (is (= 8 (sut/highest-scenic-score sample)))
  (is (= 671160 (sut/highest-scenic-score input))))

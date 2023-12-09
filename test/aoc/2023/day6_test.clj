(ns aoc.2023.day6-test
  (:require [aoc.2023.day6 :as sut]
            [clojure.test :refer [deftest is] :as t]))

(def races
  (->> [ 296   1928   1236   1391]
       (map vector [ 48     93     85     95])))

(def sample
  [[7 9] [15 40] [30 200]])

(deftest part-1
  (is (= 288 (sut/multiply-winning-ways sample)))
  (is (= 2756160 (sut/multiply-winning-ways races))))

(def sample-2 [[71530 940200]])


(def races-2 [[48938595M 296192812361391M]])

(deftest part-2
  (is (= 71503 (sut/multiply-winning-ways sample-2)))
  (is (= 34788142M (sut/multiply-winning-ways races-2))))


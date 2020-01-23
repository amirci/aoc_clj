(ns aoc.2019.day12-test
  (:require [aoc.2019.day12 :as sut]
            [clojure.test :refer [deftest is]]))

(def planets
  [[-5, 6, -11]
   [-8, -4, -2]
   [1, 16, 4]
   [11, 11, -4]])

(def sample-planets
  [[-1 0 2   ]
   [2 -10 -7 ]
   [4 -8 8   ]
   [3  5 -1   ]])

(deftest time-steps-test
  (is (= (map #(sut/mk-planet % [0 0 0]) sample-planets)
         (first (sut/moon-time-steps sample-planets)))))

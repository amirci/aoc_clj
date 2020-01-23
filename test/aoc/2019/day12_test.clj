(ns aoc.2019.day12-test
  (:require [aoc.2019.day12 :as sut]
            [clojure.test :refer [deftest is]]))

(def planets
  [[-5, 6, -11]
   [-8, -4, -2]
   [1, 16, 4]
   [11, 11, -4]])

(def sample-planets
  [[-1 0  2 ]
   [2 -10 -7]
   [4 -8  8 ]
   [3  5  -1]])

(deftest time-steps-test
  (is (= (map #(sut/mk-planet % [0 0 0]) sample-planets)
         (first (sut/moon-time-steps sample-planets))))
  (is (= [(sut/mk-planet [ 2, -1,  1 ] [  3, -1, -1 ])
          (sut/mk-planet [ 3, -7, -4 ] [  1,  3,  3 ])
          (sut/mk-planet [ 1, -7,  5 ] [ -3,  1, -3 ])
          (sut/mk-planet [ 2,  2,  0 ] [ -1, -3,  1 ])]
         (first (drop 1 (sut/moon-time-steps sample-planets))))) )

(ns aoc.day15-test
  (:require [clojure.test :refer :all]
            [aoc.day15 :refer :all]))

(def input
  {:Frosting {:capacity 4 :durability -2 :flavor 0 :texture 0 :calories 5}
   :Candy {:capacity 0 :durability 5 :flavor -1 :texture 0 :calories 8}
   :Butterscotch {:capacity -1 :durability 0 :flavor 5 :texture 0 :calories 6}
   :Sugar {:capacity 0 :durability 0 :flavor -2 :texture 2 :calories 1}})


(def sample
  {:Butterscotch {:capacity -1 :durability -2 :flavor 6 :texture 3 :calories 8}
   :Cinnamon {:capacity 2 :durability 3 :flavor -2 :texture -1 :calories 3}})


(deftest example-matches-results
  (is (= 62842880 (properties [[-1 2] [-2 3] [6 -2] [3 -1]] [44 56]))))


(deftest part-a-find-best-cookie
  (is (= 18965440 (best-cookie input))))

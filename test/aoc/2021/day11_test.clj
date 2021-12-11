(ns aoc.2021.day11-test
  (:require [aoc.2021.day11 :as sut]
            [clojure.test :as t :refer [deftest is]]))

(def input
  [[6 6 3 6 8 2 7 4 6 5]
   [6 7 7 4 2 4 8 4 3 1]
   [4 2 2 7 3 8 6 3 6 6]
   [7 4 4 7 4 5 2 6 1 3]
   [6 2 2 3 1 2 2 5 4 5]
   [2 8 1 4 3 8 8 7 6 6]
   [6 6 1 5 5 5 1 1 4 4]
   [4 8 3 6 2 3 5 8 3 6]
   [5 3 3 4 7 8 3 2 5 6]
   [4 1 2 8 3 4 4 8 4 3]])


(def sample
  [[5 4 8 3 1 4 3 2 2 3]
   [2 7 4 5 8 5 4 7 1 1]
   [5 2 6 4 5 5 6 1 7 3]
   [6 1 4 1 3 3 6 1 4 6]
   [6 3 5 7 3 8 5 4 7 8]
   [4 1 6 7 5 2 4 6 4 5]
   [2 1 7 6 8 4 1 7 2 1]
   [6 8 8 2 8 8 1 1 3 4]
   [4 8 4 6 8 4 8 5 5 4]
   [5 2 8 3 7 5 1 5 2 6]])


(def expected-step-1
  [[6 5 9 4 2 5 4 3 3 4]
   [3 8 5 6 9 6 5 8 2 2]
   [6 3 7 5 6 6 7 2 8 4]
   [7 2 5 2 4 4 7 2 5 7]
   [7 4 6 8 4 9 6 5 8 9]
   [5 2 7 8 6 3 5 7 5 6]
   [3 2 8 7 9 5 2 8 3 2]
   [7 9 9 3 9 9 2 2 4 5]
   [5 9 5 7 9 5 9 6 6 5]
   [6 3 9 4 8 6 2 6 3 7]])


(defn print-octopi
  [octopi]
  (println "\n---")
  (doseq [o octopi]
    (println o)))

(->> {:pending sut/all-pts
      :flashed #{}
      :octopi (sut/inc-level expected-step-1)}
     (iterate sut/flash-it)
     (take 4)
     (map :octopi)
     (map print-octopi))


(def expected-step-2
  [[8 8 0 7 4 7 6 5 5 5]
   [5 0 8 9 0 8 7 0 5 4]
   [8 5 9 7 8 8 9 6 0 8]
   [8 4 8 5 7 6 9 6 0 0]
   [8 7 0 0 9 0 8 8 0 0]
   [6 6 0 0 0 8 8 9 8 9]
   [6 8 0 0 0 0 5 9 4 3]
   [0 0 0 0 0 0 7 4 5 6]
   [9 0 0 0 0 0 0 8 7 6]
   [8 7 0 0 0 0 6 8 4 8]])

(def expected-step-3
  [[0 0 5 0 9 0 0 8 6 6]
   [8 5 0 0 8 0 0 5 7 5]
   [9 9 0 0 0 0 0 0 3 9]
   [9 7 0 0 0 0 0 0 4 1]
   [9 9 3 5 0 8 0 0 6 3]
   [7 7 1 2 3 0 0 0 0 0]
   [7 9 1 1 2 5 0 0 0 9]
   [2 2 1 1 1 3 0 0 0 0]
   [0 4 2 1 1 2 5 0 0 0]
   [0 0 2 1 1 1 9 0 0 0]])


(def expected-step-10
  [[0 4 8 1 1 1 2 9 7 6]
   [0 0 3 1 1 1 2 0 0 9]
   [0 0 4 1 1 1 2 5 0 4]
   [0 0 8 1 1 1 1 4 0 6]
   [0 0 9 9 1 1 1 3 0 6]
   [0 0 9 3 5 1 1 2 3 3]
   [0 4 4 2 3 6 1 1 3 0]
   [5 5 3 2 2 5 2 3 5 0]
   [0 5 3 2 2 5 0 6 0 0]
   [0 0 3 2 2 4 0 0 0 0]])


(defn after-steps
  [octopi n]
  (->> {:octopi octopi}
       sut/steps
       (drop n)
       first
       :octopi))

(defn a-step
  [octopi]
  (->> {:octopi octopi}
       sut/octo-step
       :octopi))

(deftest part-1
  (is (= expected-step-1 (a-step sample)))
  (is (= expected-step-2 (a-step expected-step-1)))
  (is (= expected-step-3 (a-step expected-step-2)))
  (is (= expected-step-2 (after-steps sample 2)))
  (is (= expected-step-10 (after-steps sample 10)))
  (is (= 204 (sut/count-flashes sample 10)))
  (is (= 1656 (sut/count-flashes sample 100)))
  (is (= 1585 (sut/count-flashes input 100))))

(deftest part-2
  (is (= 195 (sut/sync-flash sample)))
  (is (= 382 (sut/sync-flash input))))


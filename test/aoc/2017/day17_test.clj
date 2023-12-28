(ns aoc.2017.day17-test
  (:require [aoc.2017.day17 :as sut]
            [clojure.test :refer [deftest is]]))


(def input 337)


(deftest ^:slow part-a
  (is (= 600 (sut/spinlock input 2017))))

(deftest ^:slow part-b
  (is (= 31220910 (sut/spinlock-angry input 50000000))))


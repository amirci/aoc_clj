(ns aoc.2017.day17-test
  (:require [aoc.2017.day17 :as sut]
            [clojure.test :refer [deftest testing is]]))


(def input 337)


(deftest part-a
  (testing "input"
    (is (= 600 (sut/spinlock input 2017)))))

(deftest ^:slow part-b
  (testing "input"
    (is (= 31220910 (sut/spinlock-angry input 50000000)))))


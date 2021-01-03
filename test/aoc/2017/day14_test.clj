(ns aoc.2017.day14-test
  (:require [aoc.2017.day14 :as sut]
            [clojure.tools.trace :refer [trace]]
            [clojure.test :refer [deftest testing is]]))

(def input "wenycdww")

(def sample "flqrgnkx")

(deftest ^:slow part-a
  (testing "sample"
    (is (= 8108 (sut/count-squares sample))))

  (testing "input"
    (is (= 8226 (sut/count-squares input)))))


(deftest ^:slow part-b
  #_(testing "sample"
    (is (= 1242 (sut/count-regions sample))))

  (testing "input"
    (is (= 1128 (sut/count-regions input)))))


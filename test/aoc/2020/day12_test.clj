(ns aoc.2020.day12-test
  (:require [aoc.2020.day12 :as sut]
            [clojure.java.io :as io]
            [clojure.test :as t :refer :all]))


(def input
  (->> "2020/day12.input.txt"
       io/resource
       slurp
       clojure.string/split-lines))


(def sample-a
  ["F10" "N3" "F7" "R90" "F11"])


(deftest part-a
  (testing "sample"
    (is (= 25 (sut/distance sample-a))))

  (testing "input"
    (is (= 562 (sut/distance input)))))

(deftest part-b
  (testing "sample"
    (is (= 286 (sut/dist-wp sample-a))))

  (testing "input"
    (is (= 101860 (sut/dist-wp input)))))


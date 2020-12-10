(ns aoc.2020.day10-test
  (:require [aoc.2020.day10 :as sut]
            [clojure.java.io :as io]
            [clojure.test :as t :refer :all]))

(def input
  (->> "2020/day10.input.txt"
       io/resource
       slurp
       clojure.string/split-lines
       (map clojure.edn/read-string)))

(def sample-a-large
  [28 33 18 42 31 14 46 20 48 47 24 23
   49 45 19 38 39 11 1 32 25 35 8 17 7
   9 4 2 34 10 3])

(def sample-a
  [16 10 15 5 1 11 7 19 6 12 4])

(deftest part-a
  (testing "sample large"
    (is (= {1 22 3 10} (sut/jolt-diff sample-a-large)))
    (is (= 220 (sut/mult-jolt-diff sample-a-large))))
  (testing "input"
    (is (= 2376 (sut/mult-jolt-diff input)))))


(deftest part-b
  (testing "sample"
    (is (= 8 (sut/arrangements sample-a))))
  (testing "sample"
    (is (= 19208 (sut/arrangements sample-a-large))))
  (testing "input"
    (is (= 129586085429248 (sut/arrangements input)))))


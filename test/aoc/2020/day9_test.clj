(ns aoc.2020.day9-test
  (:require [aoc.2020.day9 :as sut]
            [clojure.java.io :as io]
            [clojure.test :as t :refer :all]))

(def input
  (->> "2020/day9.input.txt"
       io/resource
       slurp
       clojure.string/split-lines
       (map clojure.edn/read-string)))

(def sample-a
  [35 20 15 25 47 40 62 55 65 95 102 117 150 182 127 219 299 277 309 576])

(deftest part-a
  (testing "sample"
    (is (= [14 127] (sut/xmas-invalid 5 sample-a))))

  (testing "input"
    (is (= [503 26134589] (sut/xmas-invalid 25 input)))))


(deftest part-b
  (testing "sample"
    (is (= 62 (sut/weakness 5 sample-a))))
  (testing "input"
    (is (= 3535124 (sut/weakness 25 input)))))


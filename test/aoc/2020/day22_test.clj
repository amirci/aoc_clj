(ns aoc.2020.day22-test
  (:require [aoc.2020.day22 :as sut]
            [clojure.java.io :as io]
            [clojure.test :as t :refer :all]))


(defn parse-player [[player rst]]
  (mapv read-string rst)
  #_(->> player
       first
       (re-matches #"Player (\d+):")
       (drop 1)
       first
       read-string))


(def input
  (->> "2020/day22.input.txt"
       io/resource
       slurp
       clojure.string/split-lines
       (partition-by empty?)
       (remove (comp empty? first))
       (map (partial split-at 1))
       (map parse-player)))


(def sample [[9 2 6 3 1] [5 8 4 7 10]])

(deftest part-a
  (testing "sample"
    (is (= 306 (sut/space-cards sample))))

  (testing "input"
    (is (= 32815 (sut/space-cards input)))))

(def infinite [[43 19] [2 29 14]])

(deftest part-b
  (testing "sample"
    (is (= 291 (sut/recursive-space-cards sample))))

  (testing "input"
    (is (= 30695 (sut/recursive-space-cards input)))))





(ns aoc.2017.day21-test
  (:require [aoc.2017.day21 :as sut]
            [clojure.java.io :as io]
            [clojure.test :refer [deftest testing is]]))

(def input-rules
  (->> "2017/day21.input.txt"
       io/resource
       slurp
       clojure.string/split-lines))


(def sample-rules
  ["../.# => ##./#../..."
   ".#./..#/### => #..#/..../..../#..#"])


(def start
  [".#."
   "..#"
   "###"])


(defn print-image [image]
  (->> image
       (map (partial apply str))
       (map println)))

(deftest part-a
  (testing "sample"
    (is (= 12 (sut/count-active-pixels start sample-rules 2))))

  (testing "input"
    (is (= 179 (sut/count-active-pixels start input-rules 5)))))

(deftest part-b
  (testing "input"
    (is (= 2766750 (sut/count-active-pixels start input-rules 18)))))




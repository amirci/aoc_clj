(ns aoc.2020.day13-test
  (:require [aoc.2020.day13 :as sut]
            [clojure.java.io :as io]
            [clojure.math.numeric-tower :as math]
            [clojure.test :as t :refer :all]))


(def input
  (->> "2020/day13.input.txt"
       io/resource
       slurp
       clojure.string/split-lines))

(def earliest (clojure.edn/read-string (first input)))


(def buses (clojure.string/split (second input) #","))


(def active
  (->> buses
       (filter (partial not= "x"))
       (map clojure.edn/read-string)))

(def sample-earliest 939)

(def sample-active [7 13 59 31 19])

(sut/find-buses buses)

(deftest part-a
  (testing "sample"
    (is (= 295 (sut/find-earliest sample-earliest sample-active))))
  (testing "input"
    (is (= 2995 (sut/find-earliest earliest active)))))


(deftest part-b
  (testing "samples"
    (is (= 3417 (sut/find-buses ["17" "x" "13" "19"])))
    (is (= 754018 (sut/find-buses ["67" "7" "59" "61"])))
    (is (= 779210 (sut/find-buses ["67" "x" "7" "59" "61"])))
    (is (= 1261476 (sut/find-buses ["67" "7" "x" "59" "61"])))
    (is (= 1202161486 (sut/find-buses ["1789","37","47","1889"]))))

  (testing "input"
    (is (= 1012171816131114 (sut/find-buses buses)))))




(ns aoc.2021.day1-test
  (:require [aoc.2022.day1 :as sut]
            [clojure.string :as s]
            [clojure.test :refer [deftest is] :as t]))

(defn read-calories [coll]
  (->> coll
       (partition-by empty?)
       (filter (comp seq first))
       (map (partial map read-string))))

(def calories-list
  (->> "resources/2022/day1.txt"
       slurp
       s/split-lines
       read-calories))

(def sample
  (->> ["1000"
        "2000"
        "3000"
        ""
        "4000"
        ""
        "5000"
        "6000"
        ""
        "7000"
        "8000"
        "9000"

        ""
        "10000"]
       read-calories))

(deftest part-1
  (is (= 24000 (sut/largest-calorie-sum sample)))
  (is (= 74198 (sut/largest-calorie-sum calories-list))))


(deftest part-2
  (is (= 45000 (sut/top3-calories-sum sample)))
  (is (= 209914 (sut/top3-calories-sum calories-list))))

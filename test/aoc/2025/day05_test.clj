(ns aoc.2025.day05-test
  (:require [aoc.2025.day05 :as sut]
            [clojure.test :as t]
            [clojure.string :as s]))

(defn parse-range [line]
  (->> (s/split line #"-" )
       (mapv read-string)))

(defn- parse-db [lines]
  (let [ranges (take-while seq lines)
        ids (->> lines (drop (inc (count ranges))))]
    [(->> ranges
          (map parse-range)
          sort
          vec)
     (map read-string ids)]))


(def sample-db
  (->> ["3-5"
        "10-14"
        "16-20"
        "12-18"
        ""
        "1"
        "5"
        "8"
        "11"
        "17"
        "32"]
       (parse-db)))


(def input-db
  (->> "resources/2025/day_05.txt"
       slurp
       s/split-lines
       parse-db))


(t/deftest part-1
  (t/is (= 3 (sut/total-fresh sample-db)))
  (t/is (= 674 (sut/total-fresh input-db))))


(t/deftest part-2
  (t/is (= 14 (sut/total-range-fresh sample-db)))
  (t/is (= 352509891817881 (sut/total-range-fresh input-db))))


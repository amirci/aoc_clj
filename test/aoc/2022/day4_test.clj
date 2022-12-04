(ns aoc.2022.day4-test
  (:require [aoc.2022.day4 :as sut]
            [clojure.string :as s]
            [clojure.test :refer [deftest is] :as t]))


(def assignment-pairs
  (->> "resources/2022/day4.txt"
       slurp
       s/split-lines
       (map sut/parse-ranges)
       ))

(def sample
  (->> ["2-4,6-8"
        "2-3,4-5"
        "5-7,7-9"
        "2-8,3-7"
        "6-6,4-6"
        "2-6,4-8"]
       (map sut/parse-ranges)))

(deftest part-1
  (is (= 2 (sut/count-included-ranges sample)))
  (is (= 487 (sut/count-included-ranges assignment-pairs)))
  )

(deftest part-2
  (is (= 4 (sut/count-overlap-ranges sample)))
  (is (= 849 (sut/count-overlap-ranges assignment-pairs)))
  )

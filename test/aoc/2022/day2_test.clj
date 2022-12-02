(ns aoc.2021.day2-test
  (:require [aoc.2022.day2 :as sut]
            [clojure.string :as s]
            [clojure.test :refer [deftest is] :as t]))

(def suggested-strategy
  (->> "resources/2022/day2.txt"
       slurp
       s/split-lines
       (map (juxt first last))))

(def sample
  (->> [[\A \Y]
        [\B \X]
        [\C \Z]]))

(deftest part-1
  (is (= 15 (sut/total-score sample)))
  (is (= 13809 (sut/total-score suggested-strategy))))


(deftest part-2
  (is (= 12 (sut/total-score-2 sample)))
  (is (= 12316 (sut/total-score-2 suggested-strategy))))

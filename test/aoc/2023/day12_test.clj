(ns aoc.2023.day12-test
  (:require [aoc.2023.day12 :as sut]
            [clojure.string :as s]
            [clojure.test :refer [deftest is] :as t]))

(def input
  (->> "resources/2023/day12.txt"
       slurp
       s/split-lines))

(def sample
  ["???.### 1,1,3"
   ".??..??...?##. 1,1,3"
   "?#?#?#?#?#?#?#? 1,3,1,6"
   "????.#...#... 4,1,1"
   "????.######..#####. 1,6,5"
   "?###???????? 3,2,1"])

(defn- unfold-springs [[springs specs]]
  (vector
   (->> (repeat 5 springs)
        (s/join "?")
        (sut/add-spaces-front-back))
   (->> (repeat 5 specs) flatten vec)))

(def unfolded-sample
  (->> sample
       (map sut/parse-record)
       (map unfold-springs)))

(def unfolded-input
  (->> input
       (map sut/parse-record)
       (map unfold-springs)))

(deftest possible-arrangements-test
  (is (= [1 4 1 1 4 10] (->> sample
                             (map sut/parse-and-add-spaces)
                             (map sut/possible-solutions*)))))

(deftest part-1
  (is (= 21 (sut/sum-possible-arrangements sample)))
  (is (= 7286 (sut/sum-possible-arrangements input))) )

(deftest possible-arrangements-unfolded-test
  (is (= [1 16384 1 16 2500 506250] (->> unfolded-sample
                                         (map sut/possible-solutions*)))))

(deftest part-2
  (is (= 525152 (sut/sum-possible-arrangements-parsed unfolded-sample)))
  (is (= 25470469710341 (sut/sum-possible-arrangements-parsed unfolded-input))))


(ns aoc.2016.day3-test
  (:require [clojure.test :refer :all]
            [aoc.2016.day3 :refer :all]))

(def triangles
  (let [to-int #(Integer. %)
        lines (-> "resources/2016/day3.input.txt" slurp clojure.string/split-lines)]
    (->> lines
         (map clojure.string/trim)
         (map #(clojure.string/split % #"\ +"))
         (map #(map to-int %)))))

(deftest sample-not-triagle
  (is (not (triangle? [5 10 25]))))


(deftest possible-triangles
  (is (= 982 (possible-count triangles))))

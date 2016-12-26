(ns aoc.2016.day24-test
  (:require [clojure.test :refer :all]
            [aoc.2016.day24 :refer :all]))


(defn to-cells
  [y row]
  (map-indexed (fn [x c] [[x y] c]) row))

(def maze
  (->> "2016/day24.input.txt"
      clojure.java.io/resource
      slurp
      clojure.string/split-lines
      (map-indexed to-cells)
      (apply concat)
      (into {})))


(deftest part-a
  (is (= 430 (time (min-visit-all-points maze all-points-from-zero)))))


(deftest part-b
  (is (= 700 (time (min-visit-all-points maze all-points-from-zero-and-back)))))


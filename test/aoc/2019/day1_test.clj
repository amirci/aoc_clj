(ns aoc.2019.day1-test
  (:require
   [aoc.2019.day1 :as sut]
   [clojure.java.io :as io]
   [clojure.edn :as edn]
   [clojure.test :as t :refer [deftest is]]))



(def input (io/resource "2019/day1.input.txt"))

(defn read-modules
  []
  (->> input
       slurp
       clojure.string/split-lines
       (map edn/read-string)))

(deftest part-a
  (is (= 3266516
         (sut/modules-total-fuel (read-modules)))))


;; part B - additional fuel for each calculation

(deftest calc-with-additional-fuel
  (is (= 2     (sut/calc-fuel-additional 14)))
  (is (= 966   (sut/calc-fuel-additional 1969)))
  (is (= 50346 (sut/calc-fuel-additional 100756))))

(deftest part-b
  (is (= 4896902
         (sut/modules-total-fuel-additional (read-modules))
         )))

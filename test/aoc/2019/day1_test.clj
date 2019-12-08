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

(ns aoc.2019.day8-test
  (:require
   [aoc.2019.day8 :as sut]
   [clojure.test :refer [testing]]
   [clojure.test.check.clojure-test :refer [defspec]]
   [clojure.test.check.generators :as g]
   [clojure.test.check.properties :as prop]
   [clojure.java.io :as io]
   [clojure.edn :as edn]
   [clojure.test :as t :refer [deftest is]]))

(def message
  (-> "2019/day8.input.txt"
      io/resource
      slurp
      clojure.string/split-lines
      first))

(def width 25)

(def height 6)

(def layer-length (* width height))


(deftest part-a-test
  (is (= 2806
         (sut/min-layer-total-ones-and-twos message layer-length))))


(deftest part-b-test
  (is (= ["**** ***    **  **  ***  "
          "   * *  *    * *  * *  * "
          "  *  ***     * *  * ***  "
          " *   *  *    * **** *  * "
          "*    *  * *  * *  * *  * "
          "**** ***   **  *  * ***  "]
         (sut/decode-msg message width layer-length))))

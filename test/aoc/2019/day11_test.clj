(ns aoc.2019.day11-test
  (:require [aoc.2019.day11 :as sut]
            [clojure.java.io :as io]
            [clojure.core.async :as async]
            [clojure.edn :as edn]
            [clojure.test :refer [deftest testing is]]
            [com.gfredericks.test.chuck :as tc]
            [com.gfredericks.test.chuck.clojure-test :refer [checking]]
            [taoensso.timbre :as log]
            [clojure.data :as cljd]
            [clojure.test.check.generators :as g]))

(def input (io/resource "2019/day11.input.txt"))

(def intcode
  (->> input
       slurp
       clojure.string/split-lines
       first
       (format "[%s]")
       edn/read-string))

(def left [-1 0])
(def down [0 1])
(def right [1 0])

(deftest rotattion-test
  (is (= left  (sut/rotate sut/up sut/turn-left)))
  (is (= down  (sut/rotate left   sut/turn-left)))
  (is (= right (sut/rotate down   sut/turn-left)))
  (is (= sut/up (sut/rotate right sut/turn-left)))
  (is (= right  (sut/rotate sut/up sut/turn-right)))
  (is (= down   (sut/rotate right  sut/turn-right)))
  (is (= left   (sut/rotate down   sut/turn-right)))
  (is (= sut/up (sut/rotate left   sut/turn-right))))
 

(deftest painting-test
  (is (= {:painted {[0 0] :white} :dir [-1 0] :position [-1 0]}
         (sut/paint-and-move sut/init-robot [:white sut/turn-left]))))

(deftest part-a-test
  (is (= 2211
         (sut/total-distinct-painted intcode))))

(defn ->letter
  [[row pts]]
  (let [[_ max-col] (apply max-key first pts)
        filled? (set (map first pts))]
    filled?
    (apply str
           (for [i (range 40)]
             (if (filled? i) \* \.)))))

(defn ->letters
  [lic]
  (->> lic
       (filter (comp (partial = :white) second))
       (map first)
       (group-by second)
       (into (sorted-map))
       (map ->letter)))


(deftest part-b-test
  (is (= [".****.****..**..*..*.*..*.****..**...**."
          ".*....*....*..*.*.*..*..*.*....*..*.*..*"
          ".***..***..*....**...*..*.***..*....*..."
          ".*....*....*....*.*..*..*.*....*.**.*..."
          ".*....*....*..*.*.*..*..*.*....*..*.*..*"
          ".****.*.....**..*..*..**..****..***..**."]
         (->> intcode
              sut/paint-license
              ->letters))))


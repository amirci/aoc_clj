(ns aoc.2019.day11-test
  (:require [aoc.2019.day11 :as sut]
            [clojure.java.io :as io]
            [clojure.core.async :as async]
            [clojure.edn :as edn]
            [clojure.test :refer [deftest testing is]]
            [taoensso.timbre :as log]))

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

(deftest paint-hull-test
  (let [moves-ch (sut/mk-moves-ch)
        color-ch (async/chan)
        results-ch (sut/paint-loop color-ch moves-ch sut/init-robot)]
    (async/<!! (async/onto-chan moves-ch [1 0 0 0 1 0 1 0 0 1 1 0 1 0]) )
    (let [actual (async/<!! results-ch)
          actual-colors (async/into [] color-ch)]
      (async/close! color-ch)
      (is (= {[0 0] :black [-1 0] :black [-1 1] :white
              [0 1] :white [1 0] :white [1 -1] :white}
             (:painted actual)))
      (is (= [:black :black :black :white :black :black :black]
             (async/<!! actual-colors ))))))

(deftest part-a-test
  (is (= 2211
         (sut/total-distinct-painted intcode))))

 

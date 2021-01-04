(ns aoc.2017.day20-test
  (:require [aoc.2017.day20 :as sut]
            [clojure.java.io :as io]
            [clojure.test :refer [deftest testing is]]))


(def input
  (->> "2017/day20.input.txt"
       io/resource
       slurp
       clojure.string/split-lines))


(def sample
  ["p=< 3,0,0>, v=< 2,0,0>, a=<-1,0,0>"
   "p=< 4,0,0>, v=< 0,0,0>, a=<-2,0,0>"])


(deftest part-a
  (is (= 344 (sut/closest-particle input))))



(def sample-b
  ["p=<-6,0,0>, v=< 3,0,0>, a=< 0,0,0>"
   "p=<-4,0,0>, v=< 2,0,0>, a=< 0,0,0>"
   "p=<-2,0,0>, v=< 1,0,0>, a=< 0,0,0>"
   "p=< 3,0,0>, v=<-1,0,0>, a=< 0,0,0>"])

(deftest part-b
  (is (= 404 (sut/remove-collisions input))))


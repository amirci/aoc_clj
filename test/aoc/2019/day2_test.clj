(ns aoc.2019.day2-test
  (:require
   [aoc.2019.day2 :as sut]
   [clojure.java.io :as io]
   [clojure.edn :as edn]
   [clojure.test :as t :refer [deftest is]]))



(def input (io/resource "2019/day2.input.txt"))


(def intcode
  (->> input
       slurp
       (format "[%s]")
       edn/read-string))

(def ex1 [1,9,10,3,2,3,11,0,99,30,40,50])

(deftest run-program-test
  (is (= [4 (assoc ex1 3 70)] (sut/run-instruction [0 ex1])))
  (is (= 2 (sut/run-program [1,0,0,0,99])))
  (is (= 3500 (sut/run-program ex1))))

(deftest part-a-test
  (is (= 3562624 (sut/run-program-1202 intcode))))

(deftest part-b-test
  (is (= 1202
         (sut/find-noun-verb intcode 3562624)))
  (is (= 8298
         (sut/find-noun-verb intcode 19690720))))

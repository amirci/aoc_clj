(ns aoc.2019.day5-test
  (:require
   [aoc.2019.day5 :as sut]
   [clojure.java.io :as io]
   [clojure.edn :as edn]
   [clojure.test :as t :refer [deftest is]]))

(def input (io/resource "2019/day5.input.txt"))

(def intcode
  (->> input
       slurp
       (format "[%s]")
       edn/read-string))


(deftest parse-modifiers-test
  (is (= [\1 \1 \1 \0]
         (sut/parse-modifiers 1101))))

(deftest parse-op-test
  (is (= [[\2 4 3 4 33 3 33] 4]
         (sut/parse-op {:ptr 0 :runtime {:memory [1002,4,3,4,33]}})))
  (is (= [[\1 225 6 6 1 1100 6] 6]
         (sut/parse-op {:ptr 2 :runtime {:memory [3 8 1 8 6 6 1100 1 1 225]}}))))

(def program-2 {:ptr 2 :runtime {:memory (assoc intcode 225 1) :input 1 :outputs []}})

(deftest eval-op-test
  (is (= {:input 88 :memory [0 1 2 3 4 5 6 7 8 9 88 11 12]}
         (sut/eval-op [\3 10 0 0 5 5 5] {:input 88 :memory [0 1 2 3 4 5 6 7 8 9 10 11 12]})))
  (is (= (-> program-2
             :runtime
             (update :memory assoc  6 1101))
         (sut/eval-op [\1 225 6 6 1 1100 1100] (:runtime program-2)))))


(deftest running-instruction-test
  (is (= {:input nil :outputs [] :memory [0 1 2 3 4 5 6 7 8 9 88 11 12]}
         (sut/run-instruction {:ptr 0 :runtime {:memory  [1002,4,3,4,2] :input nil :outputs [] }})))
  (is (= 1
         (sut/run-instruction {:ptr 2 :runtime {:memory (assoc intcode 225 1) :input 1 :outputs []}})
         )))

(def program-3 
  (->> {:ptr 0 :runtime {:memory intcode :input 1 :outputs []}}
       (iterate sut/run-instruction)
       (drop 2)
       first))

(deftest part-a-test
  (is (= 15508323
         (sut/run-program-last-ouput intcode))))



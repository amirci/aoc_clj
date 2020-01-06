(ns aoc.2019.day5-test
  (:require [aoc.2019.day5 :as sut]
            [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.test :as t :refer [deftest is]]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.generators :as g]
            [clojure.test.check.properties :as prop]))

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
  (is (= {:op \2 :modifiers [\0 \1 \0] :params [4 3 4]
          :actual [{:ptr 4} 3 {:ptr 4}]
          :next 4}
         (sut/parse-op (sut/->instruction [1002,4,3,4,33]))))
  (is (= {:op \1 :modifiers [\0 \0 \0] :params [8 6 6]
          :actual [{:ptr 8} {:ptr 6} {:ptr 6}]
          :next 6}
         (-> [3 8 1 8 6 6 1100 1 1 225]
             sut/->instruction
             (assoc :ptr 2)
             sut/parse-op))))

(def program-2 {:ptr 2 :runtime {:memory (assoc intcode 225 1) :input 1 :outputs []}})


(deftest part-a-test
  (is (= 6069343 
         (sut/run-program-last-ouput intcode))))


(defn run-it
  [code input]
  (get
    (sut/run-program code input)
    :outputs))

(defspec part-b-sample-1
  100
  (prop/for-all [[input expected] (g/let [i g/pos-int] [i (if (= i 8) 1 0)])]
                 (is (= [expected] (run-it [3,9,8,9,10,9,4,9,99,-1,8] input)))))

(defspec part-b-sample-2
  100
  (prop/for-all [[input expected] (g/let [i g/pos-int] [i (if (< i 8) 1 0)])]
                (is (= [expected] (run-it [3,9,7,9,10,9,4,9,99,-1,8] input)))))

(defspec part-b-sample-3
  100
  (prop/for-all [[input expected] (g/let [i g/pos-int] [i (if (zero? i) 0 1)])]
                (is (= [expected] (run-it [3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9] input)))))

(def sample-4
  [3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99])

(defspec part-b-sample-4
  100
  (prop/for-all [[input expected] (g/let [i g/pos-int] [i (cond
                                                            (< i 8) 999
                                                            (= 8 i) 1000
                                                            (> i 8) 1001)])]
                (is (= [expected] (run-it sample-4 input)))))


(deftest part-b-test
  (is (= [3188550]
         (:outputs (sut/run-program intcode 5)))))



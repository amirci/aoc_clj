(ns aoc.2018.day8-test
  (:require [clojure.test :refer :all]
            [aoc.2018.day8 :as dut]))

(def input
  (-> "resources/2018/day8.input.txt"
      slurp
      clojure.string/split-lines
      first
      (as-> nbrs (str "[" nbrs "]"))
      read-string))

(def sample-input [2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2])

; iter 1
:pending {:chld-count 2 :mtd-count 3 :children []}

; iter 2
:pending {:chld-count 1 :mtd-count 3
          :children {:mtd-count 3 :metadata [10 11 12]}}

; iter 4
:pending {:chld-count 0 :mtd-count 3
          :children [{:mtd-count 3 :metadata [10 11 12]}
                     {:mtd-count 1 :chld-count 1 :children []}]}

; iter 5
:pending {:chld-count 0 :mtd-count 3
          :children [{:mtd-count 3 :metadata [10 11 12]}
                     {:mtd-count 0 :chld-count 1 :children [{:metadata [99] :mtd-count 1}]}]}

(def tree {:children [{:metatada [10 11 12] :children []}
                      {:metadata [2] :children [{:metadata [99]}]}]
           :metadata [1 1 2]})

(deftest sum-finished-parents-test
  (is (= [138 [] []]
         (dut/sum-finished-parents 132 [[0 3] [0 1]] (drop 0 [2 1 1 2])))))

(deftest part-a-sample-test
  (is (= 138 (dut/part-a sample-input))))

(deftest part-a-test
  (is (= 40984 (dut/part-a input))))


(deftest part-b-sample-test
  (is (= 66
         (dut/part-b input))))

*e
  (->> [[] sample-input]
       (iterate dut/read-tree-node)
       (take 5)
       (map println))


(dut/read-tree sample-input)
(concat (map :metadata (tree-seq (comp seq :children) :children (first (dut/read-tree sample-input)))))

;(map :metadata (tree-seq (comp seq :children) :children (dut/parse-tree sample-input)))
(dut/parse-tree sample-input)
;(:metadata (dut/parse-tree sample-input))
;(:children (dut/parse-tree sample-input))
;{:children 
; [{:children [], :metadata (10 11 12), :chld-count 0, :mtd-count 3} 
;  {:children 
;   [{:children [], :metadata (99), :chld-count 0, :mtd-count 1} 
;    {:children [], :metadata (99), :chld-count 0, :mtd-count 1}], 
;   :metadata (2), :chld-count 0, :mtd-count 1}], 
; :metadata (1 1 2), :chld-count 0, :mtd-count 3}

(ns aoc.2023.day20-test
  (:require [aoc.2023.day20 :as sut]
            [clojure.string :as s]
            [clojure.math.numeric-tower :as nt]
            [clojure.test :refer [deftest is] :as t]))


(def input
  (->> "resources/2023/day20.txt"
       slurp
       s/split-lines))


(def sample
  ["broadcaster -> a, b, c"
   "%a -> b"
   "%b -> c"
   "%c -> inv"
   "&inv -> a"])


(def sample2
  ["broadcaster -> a"
   "%a -> inv, con"
   "&inv -> b"
   "%b -> con"
   "&con -> output"])


(deftest part1-test
  (is (= 32000000 (sut/mul-low-high sample 1000)))
  (is (= 11687500 (sut/mul-low-high sample2 1000)))
  (is (= 912199500 (sut/mul-low-high input 1000))))



(deftest part-2
  (is (= 237878264003759
         (reduce nt/lcm [3889 3907 3911 4003]))))

;; did graphviz
;; 4 subgraphs with a conj on lr, vr, gt & nl
;; is off by one
;; lr from ns on 3888
;; vr from ck on 3906
;; gt from hh on 3910
;; nl from kz on 4002
;; lr from ns on 7777
;; vr from ck on 7813
;; gt from hh on 7821
;; nl from kz on 8005
;; lr from ns on 11666
;; vr from ck on 11720
;; gt from hh on 11732
;; nl from kz on 12008
;; lr from ns on 15555
;; vr from ck on 15627
;; gt from hh on 15643


(ns aoc.2020.day14-test
  (:require [aoc.2020.day14 :as sut]
            [clojure.java.io :as io]
            [clojure.test :as t :refer :all]))


(def input
  (->> "2020/day14.input.txt"
       io/resource
       slurp
       clojure.string/split-lines))


(def sample-a
  ["mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X"
   "mem[8] = 11"
   "mem[7] = 101"
   "mem[8] = 0"])

(deftest part-a
  (testing "sample"
    (is (= 165 (sut/sum-memory sample-a))))

  (testing "input"
    (is (= 7611244640053 (sut/sum-memory input)))))


(def sample-b
  ["mask = 000000000000000000000000000000X1001X"
   "mem[42] = 100"
   "mask = 00000000000000000000000000000000X0XX"
   "mem[26] = 1"])

(deftest part-b
  (testing "sample"
    (is (= "000000000000000000000000000000X1101X"
           (apply str (sut/addr-mask "000000000000000000000000000000X1001X" 42))))
    (is (= {26 100 27 100 58 100 59 100}
           (sut/apply-mask-v2 "000000000000000000000000000000X1001X" [42 100])))
    (is (= 208 (sut/sum-memory-v2 sample-b))))

  (testing "input"
    (is (= 3705162613854 (sut/sum-memory-v2 input)))))

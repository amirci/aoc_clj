(ns aoc.2016.day23-test
  (:require [clojure.test :refer :all]
            [aoc.2016.day23 :refer :all]))


(def instructions
  (->> "2016/day23.input.txt"
      clojure.java.io/resource
      slurp
      clojure.string/split-lines
      vec))


(def sample-instructions
  ["cpy 2 a"
   "tgl a"
   "tgl a"
   "tgl a"
   "cpy 1 a"
   "dec a"
   "dec a"])


(deftest part-a-sample
  (let [{a "a" :as regs} (run-assembunny sample-instructions {})]
    (is (= 3 a))))

(deftest part-a
  (let [{a "a" :as regs} (run-assembunny instructions {"a" 7})]
    (is (= 11424 a))))

(deftest part-b
  (is (= 479007984 (run-assembunny2 12))))


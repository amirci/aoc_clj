(ns aoc.2018.day20-test
  (:require  
    [clojure.test :refer :all]
    [blancas.kern.core :as k]
    [aoc.2018.day20 :as dut]))


(def input
  (-> "resources/2018/day20.input.txt"
      slurp))


(deftest sample-tests
  (is (= "N"
         (k/value dut/regexp "^ENWWW$")
         )))


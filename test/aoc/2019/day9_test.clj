(ns aoc.2019.day9-test
  (:require [aoc.2019.day9 :as sut]
            [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.test :as t :refer [deftest is]]))

(def intcode
  (->> "2019/day9.input.txt"
       io/resource
       slurp
       clojure.string/split-lines
       first
       (format "[%s]")
       edn/read-string))

(def program-sample-1
  [109,1,           ; rel-base ++
   204,-1,          ; write output address rel -1
   1001,100,1,100,  ; a100 = a100 + 1
   1008,100,16,101, ; if a100 == 16 then a101 = 1 else a101 = 0
   1006,101,0,
   99])

(deftest part-a-samples-test
  (is (= program-sample-1
         (-> program-sample-1
             sut/run-program
             :outputs)))

  (is (= 16
         (-> [1102,34915192,34915192,7,4,7,99,0]
             sut/run-program
             :outputs
             first
             str
             count)))

  (is (= 1125899906842624
         (-> [104,1125899906842624,99]
             sut/run-program
             :outputs
             first))))

(deftest part-a-test
  (is (= [3241900951]
         (-> intcode
             sut/run-program
             :outputs))))


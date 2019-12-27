(ns aoc.2019.day4-test
  (:require
   [aoc.2019.day4 :as sut]
   [clojure.java.io :as io]
   [clojure.edn :as edn]
   [clojure.test :as t :refer [deftest is]]))

(def input-range (range 271973 785961))


(deftest valid-pwd-test
  (is (sut/valid-pwd? 123356))
  (is (not (sut/valid-pwd? 1234567)))
  (is (sut/valid-pwd? 111111))
  (is (not (sut/valid-pwd? 123780)))
  (is (not (sut/valid-pwd? 223450))))


(deftest part-a-test
  (is (= 925
         (sut/total-valid-pwd input-range))))

(deftest part-b-test
  (is (= 607
         (sut/total-valid-pwd-2 input-range)
         )))

(ns aoc.2020.day21-test
  (:require [aoc.2020.day21 :as sut]
            [clojure.java.io :as io]
            [clojure.test :as t :refer :all]))

(def input
  (->> "2020/day21.input.txt"
       io/resource
       slurp
       clojure.string/split-lines))


(def sample
  ["mxmxvkd kfcds sqjhc nhms (contains dairy, fish)"
   "trh fvjkl sbzzf mxmxvkd (contains dairy)"
   "sqjhc fvjkl (contains soy)"
   "sqjhc mxmxvkd sbzzf (contains fish)"])



(deftest part-a
  (testing "sample"
    (is (= 5 (sut/times-non-allergen sample))))

  (testing "input"
    (is (= 2428 (sut/times-non-allergen input)))) )


(deftest part-b
  (testing "sample"
    (is (= "mxmxvkd,sqjhc,fvjkl" (sut/canonical-list sample))))

  (testing "input"
    (is (= "bjq,jznhvh,klplr,dtvhzt,sbzd,tlgjzx,ctmbr,kqms" (sut/canonical-list input)))) )



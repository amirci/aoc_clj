(ns aoc.2020.day1-test
  (:require [aoc.2020.day2 :as sut]
            [clojure.java.io :as io]
            [clojure.test :as t :refer :all]))

(def input
  (->> "2020/day2.input.txt"
       io/resource
       slurp
       clojure.string/split-lines))


(def example
  ["1-3 a: abcde"
   "1-3 b: cdefg"
   "2-9 c: ccccccccc"])

(deftest part-a

  (testing "Converting to policy"
    (is (= [1 3 "a" "abcde"] (sut/->policy "1-3 a: abcde"))))

  (testing "Checking the example"
    (is (= 2 (sut/count-valid-pwds example))))

  (testing "Checking with input"
    (is (= 614 (sut/count-valid-pwds input)))) )


(deftest part-b

  (testing "Checking the example"
    (is (= 1 (sut/count-valid-new-policy example))))

  (testing "Checking with input"
    (is (= 354 (sut/count-valid-new-policy input)))))

(ns aoc.2016.day17-test
  (:require [clojure.test :refer :all]
            [aoc.2016.day17 :refer :all]))


(def password "yjjvjgan")

(deftest part-a-shortest-path
  (testing "With password ihgpwlah"
    (is (= "DDRRRD" (shortest-path "ihgpwlah"))))
  (testing "With password kglvqrro"
    (is (= "DDUDRLRRUDRD" (shortest-path "kglvqrro"))))
  (testing "With password ulqzkmiv"
    (is (= "DRURDRUDDLLDLUURRDULRLDUUDDDRR" (shortest-path "ulqzkmiv")))))


(deftest part-a
  (is (= "RLDRUDRDDR" (time (shortest-path password)))))


(deftest part-b-longest-path-length-samples
  (testing "sample 1"
    (is (= 370 (longest-path-length "ihgpwlah")))))

(deftest part-b
  (is (= 498 (time (longest-path-length password)))))


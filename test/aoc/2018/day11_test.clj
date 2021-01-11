(ns aoc.2018.day11-test
  (:require [aoc.2018.day11 :as sut]
            [clojure.test :refer [deftest testing is]]))


(def input-serial 7511)


(def sat18 (sut/summed-area-table 18))
(def sat (sut/summed-area-table input-serial))

(deftest part-a
  (testing "power-level"
    (is (= 4 (sut/power-level 8 [3 5])))
    (is (= -5 (sut/power-level 57 [122 79])))
    (is (= 0 (sut/power-level 39 [217 196])))
    (is (= 4 (sut/power-level 71 [101 153]))))

  (testing "largest square"
    (is (= [33 45 3 29] (sut/largest-square-of-size sat18 3)))
    (is (= [21 22 3 34] (sut/largest-square-of-size sat 3)))))


(deftest part-b
  (testing "largest-all"
    (is (= [90 269 16 113] (sut/largest-all 18)))
    (is (= [232 251 12 119] (sut/largest-all 42)))
    (is (= [235 288 13 147] (sut/largest-all input-serial)))))



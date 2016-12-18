(ns aoc.2016.day18-test
  (:require [clojure.test :refer :all]
            [aoc.2016.day18 :refer :all]))


(def first-row
  (-> "2016/day18.input.txt"
      clojure.java.io/resource
      slurp
      (clojure.string/split #"\n")
      first))


(deftest calc-row-test
  (testing "sample 1"
    (is (= "^^^...^..^" (row-of-tiles ".^^.^.^^^^")))))


(deftest part-a-sample-10-by-10
  (let [expected [".^^.^.^^^^"
                  "^^^...^..^"
                  "^.^^.^.^^."
                  "..^^...^^^"
                  ".^^^^.^^.^"
                  "^^..^.^^.."
                  "^^^^..^^^."
                  "^..^^^^.^^"
                  ".^^^..^.^^"
                  "^^.^^^..^^"]]
    (is (= expected (take 10 (tile-gen ".^^.^.^^^^"))))))

(deftest part-a-total-traps-sample
  (is (= 38 (total-safe ".^^.^.^^^^" 10))))

; rank 321
(deftest part-a
  (is (= 2035 (time (total-safe first-row 40)))))


; rank 319
;(deftest part-b
;  (is (= 20000577 (time (total-safe first-row 400000)))))


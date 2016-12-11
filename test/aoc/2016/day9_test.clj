(ns aoc.2016.day9-test
  (:require [clojure.test :refer :all]
            [aoc.2016.day9 :refer :all]))

(def instructions
  (-> "resources/2016/day9.input.txt"
      slurp
      clojure.string/split-lines
      first))

(def cdec (comp count decompress))
(def cdec2 (comp count decompress2))

;(deftest part-a-sample1
;  (is (= 6 (count (decompress "ADVENT")))))
;
;(deftest part-a-sample2
;  (is (= 7 (count (decompress "A(1x5)BC")))))
;
;(deftest part-a-sample3
;  (is (= 9 (count (decompress "(3x3)XYZ")))))
;
;(deftest part-a-sample4
;  (is (= 11 (count (decompress "A(2x2)BCD(2x2)EFG")))))
;
;; Rank 600
;(deftest part-a
;  (is (= 97714 (cdec instructions))))
;

(deftest part-b-sample-2
  (is (= 20 (decompress2 "X(8x2)(3x3)ABCY"))))

(deftest part-b-sample-3
  (is (= 241920 (decompress2 "(27x12)(20x12)(13x14)(7x10)(1x12)A"))))

;(deftest part-b
;  (is (= 10762972461 (decompress2 instructions))))


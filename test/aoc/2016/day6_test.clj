(ns aoc.2016.day6-test
  (:require [clojure.test :refer :all]
            [aoc.2016.day6 :refer :all]))

(def instructions
  (-> "resources/2016/day6.input.txt"
      slurp
      clojure.string/split-lines))

(def sample ["eedadn"
  "drvtee"
  "eandsr"
  "raavrd"
  "atevrs"
  "tsrnev"
  "sdttsa"
  "rasrtv"
  "nssdts"
  "ntnada"
  "svetve"
  "tesnvt"
  "vntsnd"
  "vrdear"
  "dvrsen"
  "enarar"])


(deftest test-given-sample-part-a
  (is (= "easter" (decode-msg sample))))

; Rank 412 (15 min)
(deftest part-a
  (is (= "qtbjqiuq" (decode-msg instructions))))

(deftest test-given-sample-part-b
  (is (= "advent" (decode-msg-b sample))))

; Rank 438 (18 min)
(deftest part-b
  (is (= "akothqli" (decode-msg-b instructions))))


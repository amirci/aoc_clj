(ns aoc.2020.day4-test
  (:require [aoc.2020.day4 :as sut]
            [clojure.java.io :as io]
            [clojure.test :as t :refer :all]))

(def input
(->> "2020/day4.input.txt"
     io/resource
     slurp
     clojure.string/split-lines))


(def example-a
  ["ecl:gry pid:860033327 eyr:2020 hcl:#fffffd"
   "byr:1937 iyr:2017 cid:147 hgt:183cm"
   ""
   "iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884"
   "hcl:#cfa07d byr:1929"
   ""
   "hcl:#ae17e1 iyr:2013"
   "eyr:2024"
   "ecl:brn pid:760753108 byr:1931"
   "hgt:179cm"
   ""
   "hcl:#cfa07d eyr:2025 pid:166559648"
   "iyr:2011 ecl:brn hgt:59in"])

(deftest part-a
  (testing "sample"
    (is (= 2 (sut/valid-passports example-a))))
  (testing "input"
    (is (= 202 (sut/valid-passports input)))))


(deftest part-b
  (testing "sample"
    (is (= 2 (sut/valid-strict-passports example-a))))
  (testing "input"
    (is (= 137 (sut/valid-strict-passports input)))))

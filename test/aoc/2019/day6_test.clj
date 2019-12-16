(ns aoc.2019.day6-test
  (:require
   [aoc.2019.day6 :as sut]
   [clojure.test.check.clojure-test :refer [defspec]]
   [clojure.test.check.generators :as g]
   [clojure.test.check.properties :as prop]
   [clojure.java.io :as io]
   [clojure.test :as t :refer [deftest is]]))

(def input (io/resource "2019/day6.input.txt"))

(def input-orbits
  (-> input
      slurp
      clojure.string/split-lines))

(def sample-orbits
  ["COM)B"
   "B)C"
   "C)D"
   "D)E"
   "E)F"
   "B)G"
   "G)H"
   "D)I"
   "E)J"
   "J)K"
   "K)L"])

(def planet-gen (g/not-empty g/string-alphanumeric))

(defspec parse-orbit-test
  (prop/for-all [[p1 p2] (g/tuple planet-gen planet-gen)]
                (is (=[p1 p2] (sut/parse-orbit (format "%s)%s" p1 p2))))))

(deftest total-orbits-sample-test
  (is (= 42
         (sut/total-orbits sample-orbits))))

(deftest part-a-test
  (is (= 122782
         (sut/total-orbits input-orbits))))

(def part-b-sample
  ["COM)B"
   "B)C"
   "C)D"
   "D)E"
   "E)F"
   "B)G"
   "G)H"
   "D)I"
   "E)J"
   "J)K"
   "K)L"
   "K)YOU"
   "I)SAN"])

(deftest part-b-sample-test
  (is (= 4
         (sut/min-orbit-transfer part-b-sample))))

(deftest part-b-test
  (is (= 271
         (sut/min-orbit-transfer input-orbits))))


(ns aoc.2021.day12-test
  (:require [aoc.2021.day12 :as sut]
            [clojure.string :as st]
            [clojure.test :as t :refer [deftest is]]))

(def input
  ["LA-sn"
   "LA-mo"
   "LA-zs"
   "end-RD"
   "sn-mo"
   "end-zs"
   "vx-start"
   "mh-mo"
   "mh-start"
   "zs-JI"
   "JQ-mo"
   "zs-mo"
   "start-JQ"
   "rk-zs"
   "mh-sn"
   "mh-JQ"
   "RD-mo"
   "zs-JQ"
   "vx-sn"
   "RD-sn"
   "vx-mh"
   "JQ-vx"
   "LA-end"
   "JQ-sn"])


(def sample
  ["start-A"
   "start-b"
   "A-c"
   "A-b"
   "b-d"
   "A-end"
   "b-end"])


(def expected-paths
  ["start,A,b,A,c,A,end"
   "start,A,b,A,end"
   "start,A,b,end"
   "start,A,c,A,b,A,end"
   "start,A,c,A,b,end"
   "start,A,c,A,end"
   "start,A,end"
   "start,b,A,c,A,end"
   "start,b,A,end"
   "start,b,end"])

(def sample2
  ["dc-end"
   "HN-start"
   "start-kj"
   "dc-start"
   "dc-HN"
   "LN-dc"
   "HN-end"
   "kj-sa"
   "kj-HN"
   "kj-dc"])


(def sample3
  ["fs-end"
   "he-DX"
   "fs-he"
   "start-DX"
   "pj-DX"
   "end-zg"
   "zg-sl"
   "zg-pj"
   "pj-he"
   "RW-he"
   "fs-DX"
   "pj-RW"
   "zg-RW"
   "start-pj"
   "he-WI"
   "zg-he"
   "pj-fs"
   "start-RW"])


(deftest part-1
  (is (= (set expected-paths)
         (set
          (map #(st/join "," %)
               (sut/find-paths sample)))))
  (is (= 10 (sut/path-count sample)))
  (is (= 19 (sut/path-count sample2)))
  (is (= 226 (sut/path-count sample3)))
  (is (= 4970 (sut/path-count input))))


(deftest part-2
  (is (= 36 (sut/path-count-twice sample)))
  (is (= 137948 (sut/path-count-twice input))))

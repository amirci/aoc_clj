(ns aoc.2016.day20-test
  (:require [clojure.test :refer :all]
            [aoc.2016.day20 :refer :all]))


(def blacklist
  (-> "2016/day20.input.txt"
      clojure.java.io/resource
      slurp
      clojure.string/split-lines
      (as-> lines (map #(clojure.string/split % #"\-") lines))
      (as-> pairs (map (fn [[k v]] [(Long. k) (Long. v)]) pairs))
      sort))


; rank 550
(deftest part-a
  (is (= 32259706 (time (find-lowest-ip blacklist)))))


(deftest part-b
  (is (= 113 (time (allowed-ips blacklist)))))


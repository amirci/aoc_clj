(ns aoc.2019.day13-test
  (:require [aoc.2019.day13 :as sut]
            [clojure.java.io :as io]
            [clojure.core.async :as async]
            [clojure.edn :as edn]
            [clojure.test :refer [deftest testing is]]
            [com.gfredericks.test.chuck :as tc]
            [com.gfredericks.test.chuck.clojure-test :refer [checking]]
            [taoensso.timbre :as log]
            [clojure.data :as cljd]
            [clojure.test.check.generators :as g]))

(def input (io/resource "2019/day13.input.txt"))

(def intcode
  (->> input
       slurp
       clojure.string/split-lines
       first
       (format "[%s]")
       edn/read-string))

(deftest part-a-test
  (is (= 226
         (sut/count-block-tiles intcode))))

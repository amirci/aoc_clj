#!/usr/bin/env bb

(require '[clojure.java.io :as io])


(defn -main [& args]
  ;; Implementation of main
  (when (not= 2 (count args))
    (println "Usage: setup-day year day")
    (println)
    (println "Example: setup-day 2023 9")
    (println "Creates src/aoc/2023/day9.clj and the matching test"))

  (let [[year day] args
        source (io/file (format "src/aoc/%s/day%s.clj" year day))
        test (io/file (format "test/aoc/%s/day%s_test.clj" year day))
        ]
    (spit source (format "(ns aoc.%s.day%s)" year day ))
    (spit test (format "(ns aoc.%s.day%s-test
  (:require [aoc.%s.day%s :as sut]
            [clojure.test :refer [deftest is] :as t]))" year day year day))))


(when (= *file* (System/getProperty "babashka.file"))
  (apply -main *command-line-args*))

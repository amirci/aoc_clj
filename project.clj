(defproject aoc "0.1.0-SNAPSHOT"
  :description "Advent of code in Clojure"
  :test-selectors {:default (complement :slow)
                   :slow :slow
                   :all (constantly true)}
  :dependencies [[clj-time "0.15.0"]
                 [com.taoensso/timbre "4.10.0"]
                 [digest "1.4.5"]
                 [org.blancas/kern "1.1.0"]
                 [org.clojure/clojure "1.9.0"]
                 [org.clojure/core.async "0.6.532"]
                 [org.clojure/core.match "0.3.0-alpha4"]
                 [org.clojure/math.combinatorics "0.1.5"]
                 [org.clojure/test.check "0.10.0"]
                 [com.gfredericks/test.chuck "0.2.10"]
                 [org.clojure/tools.trace "0.7.9"]
                 [quil "2.5.0"]])

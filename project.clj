(defproject aoc "0.1.0-SNAPSHOT"
  :description "Advent of code in Clojure"
  :test-selectors {:default (complement :slow)
                   :slow :slow
                   :all (constantly true)}
  :plugins [[lein-eftest "0.6.0"]]
  :eftest {#_:report #_eftest.report.junit/report
           #_#_:report-to-file "target/junit.xml"
           :test-warn-time 20000}
  :dependencies [[clj-time "0.15.0"]
                 [org.flatland/ordered "1.15.11"]
                 [com.gfredericks/test.chuck "0.2.10"]
                 [com.taoensso/timbre "4.10.0"]
                 [digest "1.4.5"]
                 [eftest "0.6.0"]
                 [flames "0.5.0"]
                 [org.blancas/kern "1.1.0"]
                 [org.clojure/clojure "1.11.0"]
                 [org.clojure/core.async "1.3.610"]
                 [org.clojure/core.match "0.3.0-alpha4"]
                 [org.clojure/data.priority-map "1.1.0"]
                 [org.clojure/math.combinatorics "0.1.6"]
                 [org.clojure/math.numeric-tower "0.0.5"]
                 [org.clojure/test.check "0.10.0"]
                 [org.clojure/tools.trace "0.7.10"]
                 [org.clojure/tools.logging "1.1.0"]
                 [quil "2.5.0"]])

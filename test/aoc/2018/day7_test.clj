(ns aoc.2018.day7-test
  (:require [clojure.test :refer :all]
            [aoc.2018.day7 :as dut]))

(def input
  (-> "resources/2018/day7.input.txt"
      slurp
      clojure.string/split-lines))

(def sample-input
  ["Step C must be finished before step A can begin."
   "Step C must be finished before step F can begin."
   "Step A must be finished before step B can begin."
   "Step A must be finished before step D can begin."
   "Step B must be finished before step E can begin."
   "Step D must be finished before step E can begin."
   "Step F must be finished before step E can begin."])


(deftest parta-sample-test
  (is (= "CABDFE"
         (dut/part-a sample-input))))

(deftest parta-test
  (is (= "HPDTNXYLOCGEQSIMABZKRUWVFJ"
         (dut/part-a input))))

(dut/parse-deps sample-input)

(def tt (-> input dut/parse-deps))

;(dut/tick [0 pp [] [(dut/mk-worker \H)]])

(tt \B) ; => A G H O Q
(tt \N) ; => D H P T
(tt \U) ; => C D E H I K M R
(tt \A) ; => E M N O P SY
(tt \H)
(tt \X)
(tt \P)
(tt \D)

(remove (comp seq tt) (keys tt))

(dut/update-finished-deps tt #{\H})

(keys tt)



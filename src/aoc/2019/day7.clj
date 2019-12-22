(ns aoc.2019.day7
  (:require
   [clojure.math.combinatorics :as combi]
   [aoc.2019.day5 :as day5]))

(defn input-queue
  [inputs]
  (let [queue (atom inputs)]
    (fn []
      (let [[i & rst] @queue]
        (reset! queue rst)
        i))))

(defn amplifier-signal
  [program inputs]
  (day5/run-program-last-ouput program (input-queue inputs)))

(def cached-amplifier-signal (memoize amplifier-signal))

(defn run-amplifier
  [program last-output thruster-cfg]
  (let [cfg [thruster-cfg last-output]]
    (cached-amplifier-signal program cfg)))

(defn thruster-signal
  [program settings]
  (reduce
    (partial run-amplifier program)
    0
    settings))

(def cfg-rng (range 5))

(defn possible-cfgs-seq
  []
  (combi/permutations [0 1 2 3 4]))

(defn max-thruster-signal
  [program]
  (reduce
    (fn [[max-signal max-cfg :as current] cfg]
      (let [signal (thruster-signal program cfg)]
        (if (< max-signal signal)
          [signal cfg]
          current
          )))
    [0]
    (possible-cfgs-seq)))

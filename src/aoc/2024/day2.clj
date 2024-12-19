(ns aoc.2024.day2
  (:require [clojure.math :as math]))


(defn same-sign-as [a]
  (fn [n] (= (math/signum a) (math/signum n))))


(def at-least-1-at-most-3 #(<= 1 (abs %) 3))


(defn rules [fst]
  (every-pred (same-sign-as fst) at-least-1-at-most-3))


(defn- matches-rules? [[fst :as diffs]]
  (->> diffs
       (every? (rules fst))))


(defn safe-report? [report]
  (->> report
       (partition 2 1)
       (map (partial apply -))
       (matches-rules?)))


(defn total-safe-reports [reports]
  (->> reports
       (filter safe-report?)
       count))


(defn remove-idx [index v]
  (vec (concat (subvec v 0 index)
               (subvec v (inc index)))))


(defn- safe-report-with-tolerance? [report]
  (->> (for [i (range 0 (count report))]
         (remove-idx i report))
       (some safe-report?)))


(defn total-tolerate-reports [reports]
  (let [grps (group-by safe-report? reports)]
    (->> (grps false)
         (filter safe-report-with-tolerance?)
         count
         (+ (count (grps true))))))

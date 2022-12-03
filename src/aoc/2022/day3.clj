(ns aoc.2022.day3
  (:require [clojure.set :as st]))


(defn- ->compartments [rs]
  (split-at (/ (count rs) 2) rs))

(defn- common-in-both [rs]
  (->> rs
       (map set)
       (apply st/intersection)
       first))

(defn- ->priority [c]
  (let [[base f] (if (Character/isUpperCase c)
                   [\A (partial + 27)]
                   [\a inc])]
    (-> (int c)
        (- (int base))
        f)))

(defn sum-priorities
  [rucksacks]
  (->> rucksacks
       (map ->compartments)
       (map common-in-both)
       (map ->priority)
       (apply +)))



(defn- find-badge [rss]
  (->> rss
       (map set)
       (apply st/intersection)
       first))

(defn sum-badges
  [rucksacks]
  (->> rucksacks
       (partition-all 3)
       (map find-badge)
       (map ->priority)
       (apply +)))

(ns aoc.2020.day2
  (:require [clojure.edn :as edn]))


(defn ->policy [line]
  (->> line
       (re-matches #"(\d+)-(\d+) (\w): (\w+)")
       (drop 1)
       (map (fn [f arg] (f arg))
            [edn/read-string edn/read-string identity identity])))

(defn valid-pwd?
  [[mn mx ltr pwd]]
  (-> (re-pattern ltr)
      (re-seq pwd)
      count
      (#(<= mn % mx))))


(defn count-valid-pwds
  [pwds]
  (->> pwds
       (map ->policy)
       (filter valid-pwd?)
       count))


(defn valid-pwd-new-policy?
  [[mn mx ltr pwd]]
  (->> [mn mx]
       (map dec)
       (keep (partial get pwd))
       (filter (partial = (first ltr)))
       count
       (= 1)))

(defn count-valid-new-policy
  [pwds]
  (->> pwds
       (map ->policy)
       (filter valid-pwd-new-policy?)
       count))



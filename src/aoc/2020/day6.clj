(ns aoc.2020.day6
  (:require [clojure.string :as s]))


(defn split-fams [forms]
  (->> forms
       (partition-by s/blank?)
       (remove (comp empty? first))))


(defn ->answers [fam]
  (->> fam
       (apply concat)
       set))


(defn total-answers [f forms]
  (->> forms
       split-fams
       (map (comp count f))
       (apply +)))

(def single-answers (partial total-answers ->answers))

(defn ->common-answers [fam]
  (->> fam
       (map set)
       (apply clojure.set/intersection)))

(def common-answers (partial total-answers ->common-answers))


(ns aoc.2016.day7
  (:require [clojure.tools.trace :refer [trace]]))

(defn abba?
  [[a b c d]]
  (and (= b c) (= a d) (not= a b)))

(defn has-abba?
  [code]
  (->> (map vector code (drop 1 code) (drop 2 code) (drop 3 code))
       (filter abba?)
       first
       nil?
       not))

(defn check
  [matches]
  (let [valid (take-nth 2 matches)
        invalid (take-nth 2 (rest matches))]
    (and (some identity valid)
         (not (some identity invalid)))))

(defn tls?
  [code]
  (->> code
       (re-seq #"[a-z]+")
       (map has-abba?)
       check))

(defn count-tls
  [codes]
  (->> codes
      (filter tls?)
      count))

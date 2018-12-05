(ns aoc.2018.day5
  (:require 
    [clojure.string :as str]))

(defn the-pairs
  [s]
  (map vector s (drop 1 s)))

(defn reactive?
  [component]
  (->> component
       sort
       reverse
       (map int)
       (apply -)
       (= 32)))

(defn rr
  [polymer i]
  (->> [i (inc i)]
       (map #(nth polymer %))
       reactive?))

(defn remove-reactive
  [i polymer]
  (if i
    (vec
      (concat
        (subvec polymer 0 i)
        (subvec polymer (+ i 2))))
    polymer))

(defn find-reactive-index
  [polymer]
  (first
    (filter (partial rr polymer)
            (range (dec (count polymer))))))

(defn reduce-component
  [[_ polymer]]
  (println ">>>" (count polymer))
  (let [i (find-reactive-index polymer)]
    (vector 
      i
      (remove-reactive i polymer))))

(defn reduce-polymer
  [polymer]
  (->> [0 (vec polymer)]
       (iterate reduce-component)
       (drop-while first)
       first
       second))

(defn part-a
  [polymer]
  (count (reduce-polymer polymer)))

(defn find-reactive
  [polymer]
  (->> polymer
       the-pairs
       (filter reactive?)
       first))


(defn remove-reactive*
  [[_ polymer]]
  (println (count polymer))
  (let [r (find-reactive polymer)]
    (vector
      r
      (if r
        (clojure.string/replace-first polymer (apply str r) "")
        polymer))))

(defn reduce-polymer*
  [polymer]
  (->> [true polymer]
       (iterate remove-reactive)
       (drop-while first)
       first
       second
       count))



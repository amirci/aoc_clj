(ns aoc.2025.day04 
  (:require [clojure.set :as st]))

(defn- neighbours* [[a b]]
  (for [x [-1 0 1] y [-1 0 1] :when (not= [x y] [0 0])]
    [(+ x a) (+ y b)]))


(def neighbours (memoize neighbours*))


(defn- has-lt-4-nbrs? [rolls pos]
  (->> pos
       neighbours
       (keep rolls)
       count
       (> 4)))


(defn find-rolls-to-lift [rolls]
  (->> rolls
       (filter (partial has-lt-4-nbrs? rolls))
       count))


(defn remove-possible-rolls [[rolls]]
  (->> rolls
       (filter (partial has-lt-4-nbrs? rolls))
       ((juxt #(st/difference rolls %) count))))


(defn remove-all-possible-rolls [rolls]
  (->> [rolls]
       (iterate remove-possible-rolls)
       (drop 1)
       (take-while (comp pos? second))
       (map second)
       (apply +)))

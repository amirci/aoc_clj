(ns aoc.2016.day25
  (:require [clojure.tools.trace :refer [trace]]
            [clojure.core.match :refer [match]]
            [clojure.string :refer [split join index-of ]]))

(def to-int #(Integer. %))

(defn get-val
  [rs v]
  (if (re-matches #"[a-z]" v)
    (rs v)
    (to-int v)))

(def not-zero? (complement zero?))

(defn run-cmd
  [cmds]
  (fn [[ptr rs output]]
    (let [cmd (nth cmds ptr) ptr+ (inc ptr)]
      (match cmd
             ["cpy" v reg] [ptr+ (assoc rs reg (get-val rs v)) output]
             ["inc" reg  ] [ptr+ (assoc rs reg (inc (rs reg))) output]
             ["dec" reg  ] [ptr+ (assoc rs reg (dec (rs reg))) output]
             ["out" reg  ] [ptr+ rs (conj output (rs reg))]
             ["jnz" v   n] [(if (not-zero? (get-val rs v)) (+ ptr (get-val rs n)) ptr+) rs output]))))


(defn run-cmds
  [init cmds]
  (iterate (run-cmd (map #(split % #" ") cmds)) [0 {"a" init} []]))

(defn matches-signal
  [cmds ptrn i]
  (->> (run-cmds i cmds)
       (drop 60000)
       first
       last
       (take (count ptrn))
       (apply str)
       (= ptrn)))

(defn find-pattern
  [ptrn cmds]
  (->> (range)
       (filter #(matches-signal cmds ptrn %))
       first))


(ns aoc.2016.day23
  (:require [clojure.tools.trace :refer [trace]]
            [clojure.core.match :refer [match]]
            [clojure.string :refer [split join index-of ]]))

(def to-int #(Integer. %))

(defn get-val
  [rs v]
  (if (re-matches #"[a-z]" v)
    (rs v)
    (to-int v)))

(defn toggle
  [ptr cmds]
  (if (>= ptr (count cmds))
    cmds
    (->> (match (nth cmds ptr)
                ["cpy" v reg] ["jnz" v reg]
                ["inc" reg  ] ["dec" reg]
                ["dec" reg  ] ["inc" reg]
                ["tgl" reg  ] ["inc" reg]
                ["jnz" reg n] ["cpy" reg n])
         (assoc cmds ptr))))

(def not-zero? (complement zero?))

(defn run-cmd
  [[ptr rs cmds]]
  (let [cmd (nth cmds ptr)]
    (match cmd
           ["cpy" v reg] [(inc ptr) (assoc rs reg (get-val rs v)) cmds]
           ["inc" reg  ] [(inc ptr) (assoc rs reg (inc (rs reg))) cmds]
           ["dec" reg  ] [(inc ptr) (assoc rs reg (dec (rs reg))) cmds]
           ["tgl" reg  ] [(inc ptr) rs (toggle (+ (rs reg) ptr) cmds)]
           ["jnz" v   n] [(if (not-zero? (get-val rs v)) (+ ptr (get-val rs n)) (inc ptr)) rs cmds])))

(defn still-running?
  [[ptr rs cmds]]
  (< ptr (count cmds)))

(defn run-assembunny
  [cmds regs]
  (->> cmds
       (map #(split % #" "))
       vec
       (conj [0 regs])
       (iterate run-cmd)
       (drop-while still-running?)
       first
       (drop 1)
       first))

(defn run-assembunny2
  [n]
  ; n! + 84 * 76
  (->> (range 1 (inc n))
       (reduce *)
       (+ 6384)))

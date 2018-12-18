(ns aoc.2018.day18
  (:require 
    [clojure.string :as str]))


(defn neighbours
  [[a b] acres]
  (for [x [-1 0 1] y [-1 0 1] :when (not= [x y] [0 0])]
    (acres [(+ a x) (+ b y)])))


; An open acre will become filled with trees if three or more adjacent acres contained trees. Otherwise, nothing happens.
; An acre filled with trees will become a lumberyard if three or more adjacent acres were lumberyards. Otherwise, nothing happens.
; An acre containing a lumberyard will remain a lumberyard if it was adjacent to at least one other lumberyard and at least one acre containing trees. Otherwise, it becomes open.

(defn magic-rules
  [acre nbrs]
  (let [frq (frequencies nbrs)
        trees  (get frq \| 0)
        lumber (get frq \# 0)
        open   (get frq \. 0)]
    (cond
      (and (= acre \.) (>= trees 3))  \|
      (and (= acre \|) (>= lumber 3)) \#
      (and (= acre \#) (or (zero? trees) (zero? lumber))) \.
      :else acre)))

(defn evolve
  [acres]
  (reduce-kv
    (fn [m pos acre]
      (->> (neighbours pos acres)
           (magic-rules acre)
           (assoc m pos)))
    {}
    acres))

(defn part-a
  [minutes acres]
  (let [{wood \| lumber \#} (->> acres
                                 (iterate evolve)
                                 (drop minutes)
                                 first
                                 vals
                                 frequencies)]
    (* wood lumber)))

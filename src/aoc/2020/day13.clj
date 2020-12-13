(ns aoc.2020.day13
  (:require
   [clojure.math.numeric-tower :as math]))


(defn find-earliest [ts active]
  (->> active
       (map #(- % (mod ts %)))
       (map vector active)
       (apply min-key second)
       (apply *)))


(defn next-ts [{:keys [ts step]} [i id :as bus]]
  (->> ts
       (iterate (partial + step))
       (drop-while #(pos? (mod (+ % i) id)))
       first
       (hash-map :step (math/lcm step id) :ts)))

(defn parse [s]
  (when s
    (clojure.edn/read-string s)))

(defn find-buses [buses]
  (->> buses
       (map (partial re-matches #"\d+"))
       (map parse)
       (map vector (range))
       (filter second)
       (reduce next-ts {:ts 0 :step 1})
       :ts))


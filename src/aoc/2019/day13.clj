(ns aoc.2019.day13
  (:require
   [clojure.core.async :as async]
   [taoensso.timbre :as log]
   [aoc.2019.day5 :as day5]))

(defn ->tile
  [n]
  (assert (<= 0 n 4))
  (nth [:empty :wall :block :h-paddle :ball] n))

(defn build-tiles
  [m [x y tile-id]]
  (assoc m [x y] (->tile tile-id)))

(defn run-game
  [game]
  (->> game
       day5/run-program
       :outputs
       (partition-all 3)
       (reduce build-tiles {})))

(defn run-game-for-free
  [game]
  (->> game
       (update 0 2)
       run-game))

(defn count-block-tiles
  [code]
  (->> code
       run-game
       (filter (comp (partial = :block) second))
       count))

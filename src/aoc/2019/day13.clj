(ns aoc.2019.day13
  (:require
   [clojure.core.async :as async]
   [taoensso.timbre :as log]
   [aoc.2019.day5 :as day5]))

(defn ->tile
  [n]
  (assert n "TILE Can't be nil!")
  (assert (<= 0 n 4) (str "TILE " n " should be 0 <= n <= 4" ))
  (nth [:empty :wall :block :h-paddle :ball] n))

(def score? (partial = [-1 0]))

(defn build-tiles
  [m [x y tile-id]]
  (if (score? [x y])
    (assoc m :score tile-id)
    (assoc m [x y] (->tile tile-id))))

(defn run-game
  [game]
  (->> game
       day5/run-program
       :outputs
       (partition-all 3)
       (reduce build-tiles {})))

(defn run-game-for-free
  [game input-fn output-fn]
  (-> game
      (assoc 0 2)
      (day5/run-program input-fn output-fn)))

(defn count-block-tiles
  [code]
  (->> code
       run-game
       (filter (comp (partial = :block) second))
       count))

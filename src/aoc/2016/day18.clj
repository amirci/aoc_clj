(ns aoc.2016.day18
  (:require [clojure.tools.trace :refer [trace]]))

(def trap \^)
(def safe \.)

;Its left and center tiles are traps, but its right tile is not.
;Its center and right tiles are traps, but its left tile is not.
;Only its left tile is a trap.
;Only its right tile is a trap.

(def is-trap?
  #{[trap trap safe]
    [safe trap trap]
    [trap safe safe]
    [safe safe trap]})

(def safe-or-tile #(if (is-trap? %) trap safe))

(defn row-of-tiles
  [prev-row]
  (let [row (str safe prev-row safe)]
    (->> row
         (partition 3 1)
         (map safe-or-tile)
         (apply str))))


(def tile-gen #(iterate row-of-tiles %))

(defn tile-count
  [tile]
  (fn [row]
    (->> row
         (filter #(= tile %))
         count)))

(def tcs (tile-count safe))

(defn total-safe
  [init n]
  (->> init
       tile-gen
       (map (tile-count safe))
       (take n)
       (reduce +)))




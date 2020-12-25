(ns aoc.2020.day24
  (:require
   [blancas.kern.core :as kc :refer :all]
   [blancas.kern.expr :as ke :refer :all]
   [blancas.kern.lexer.java-style :as lx]))


(def around
  [[1 -1 0] [1 0 -1] [0 1 -1]
   [-1 1 0] [-1 0 1] [0 -1 1]])


(def directions
  (zipmap [:e :ne :nw :w :sw :se] around))


(def tile-expr
  (->> directions
       keys
       (map name)
       (map lx/token)
       (apply <|>)
       (<$> (comp directions keyword))))


(def tile-seq-expr (many1 tile-expr))


(defn find-tile [tile-path]
  (->> tile-path
       (value tile-seq-expr)
       (apply (partial map +))))


(defn flip [tile]
  (if (= tile :black) :white :black))


(defn flip-tile [tiles tile-path]
  (let [location (find-tile tile-path)]
    (update tiles location flip)))


(def ->tiles (partial reduce flip-tile {}))


(defn flip-tiles [paths]
  (->> paths
       ->tiles
       vals
       frequencies))


; Any black tile with zero or more than 2 black tiles immediately adjacent to it is flipped to white.
; Any white tile with exactly 2 black tiles immediately adjacent to it is flipped to black.


(defn neighbors [tile]
  (map (partial map + tile) around))


(defn stay-or-flip-black? [tiles tile]
  (let [ns (->> tile neighbors (keep tiles) count)
        black? (tiles tile)]
    (or (= 2 ns)
        (and black? (= 1 ns)))))


(defn day-flip [tiles]
  (->> tiles
       (mapcat neighbors)
       (filter (partial stay-or-flip-black? tiles))
       set))


(defn artsy [days input]
  (->> input
       ->tiles
       (filter (comp (partial = :black) second))
       (map first)
       set
       (iterate day-flip)
       (drop days)
       first
       count))

(ns aoc.2024.day8
  (:require [clojure.math :as math])
  (:require [clojure.math.combinatorics :as combo]))


(defn multiset [pairs]
  (reduce
   (fn [m [a b]]
     (update m a (fnil conj #{}) b))
   {}
   pairs))


(defn- pts->vector [[x1 y1] [x2 y2]]
  [(- x2 x1) (- y2 y1)])


(defn add-pts [[x1 y1] [x2 y2]]
  [(+ x1 x2) (+ y1 y2)])


(defn antinodes [[p1 p2]]
  (let [v1 (pts->vector p1 p2)
          v2 (pts->vector p2 p1)]
      [(add-pts p2 v1) (add-pts p1 v2)]))


(defn inside? [rows cols [x y]]
  (and (< -1 x rows) (< -1 y cols)))


(defn- inside-points-in-vector [inside? p v]
  (->> p
       (iterate (partial add-pts v))
       (take-while inside?)))


(defn- t-antinodes [rows cols [p1 p2]]
  (let [v1 (pts->vector p1 p2)
        v2 (pts->vector p2 p1)
        pts-in-v (partial inside-points-in-vector (partial inside? rows cols))]
    (->> (pts-in-v p2 v1)
         (concat (pts-in-v p1 v2)))))


(defn- possible-pairs [[_ positions]]
  (combo/combinations positions 2))


(defn total-antinodes [[rows cols antennas]]
  (->> antennas
       (mapcat possible-pairs)
       (mapcat antinodes)
       (filter (partial inside? rows cols))
       set
       count))


(defn total-t-antinodes [[rows cols antennas]]
  (->> antennas
       (mapcat possible-pairs)
       (mapcat (partial t-antinodes rows cols))
       set
       count))


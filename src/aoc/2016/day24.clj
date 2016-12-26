(ns aoc.2016.day24
  (:require [clojure.tools.trace :refer [trace]]
            [clojure.math.combinatorics :as combo]
            [clojure.set :refer [map-invert rename-keys]]))

(def empty-queue clojure.lang.PersistentQueue/EMPTY)

(def find-poi
  (memoize (fn [maze]
             (->> maze
                  (filter (fn [[k v]] ((set "012234567") v)))
                  (map (fn [[k v]] [(- (int v) 48) k]))
                  (into {})))))

(defn wall? [maze p] (= (maze p) \#))

(defn available-moves
  [[x y] maze]
  (->> [[(inc x) y] [(dec x) y] [x (inc y)] [x (dec y)]]
       (filter (complement #(wall? maze %)))))

(defn search-for
  [target maze]
  (fn [[msf visited pending]]
    (let [current (peek pending) pending (pop pending) steps (visited current)]
      (cond
        (>= steps msf) [msf visited pending]
        (= target current) [(min msf steps) visited pending]
        :else (let [available (available-moves current maze)
                    new-pts (filter (complement visited) available)
                    steps+ (inc steps)]
                [msf
                 (reduce #(assoc %1 %2 steps+) visited new-pts)
                 (apply conj pending new-pts)])))))

(def pending-points? (comp not empty? last))

(def shortest-path-between
  (memoize (fn [[p1 p2] maze]
             (->> [Integer/MAX_VALUE {p1 0} (conj empty-queue p1)]
                  (iterate (search-for p2 maze))
                  (drop-while pending-points?)
                  first
                  first))))

(defn visit-all-points
  [maze poi to-visit]
  (->> to-visit
       (partition 2 1)
       (map #(sort (map poi %)))
       (map #(shortest-path-between % maze))
       (reduce +)))

(defn min-visit-all-points
  [maze visit-orders]
  (let [poi (find-poi maze)]
    (->> visit-orders
         (map #(visit-all-points maze poi %))
         (apply min))))

(def all-points-from-zero
  (->> (range 1 8)
       combo/permutations
       (map #(cons 0 %))))

(def all-points-from-zero-and-back
  (->> all-points-from-zero
       (map #(conj (vec %) 0))))


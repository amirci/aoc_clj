(ns aoc.2016.day17
  (:require [clojure.tools.trace :refer [trace]]
            [digest :refer :all]))

(def empty-queue clojure.lang.PersistentQueue/EMPTY)

(defn min-path [s p] (if (empty? s) p (min-key count s p)))

(defn max-path [s p] (if (empty? s) p (max-key count s p)))

(def open? #{\b \c \d \e \f})

(def to-coord {\U [-1 0] \D [1 0] \L [0 -1] \R [0 1]})

(def inside? (set (for [k (range 4) v (range 4)] [k v])))

(defn sum-pos [p1 p2] (map + p1 p2))

(defn valid-pos? [current c] (->> c (sum-pos current) inside?))

(def grants-vault-access? #(= [3 3] %))

(defn possible-moves
  [pwd current path]
  (->> (str pwd path)
       digest/md5
       (take 4)
       (map vector to-coord)
       (filter (fn [[[_ p] l]] (and (valid-pos? current p) (open? l))))
       (map first)))

(defn add-moves
  [pending pwd pos path]
  (->> (possible-moves pwd pos path)
       (map (fn [[step adjust]] [(sum-pos adjust pos) (str path step)]))
       (apply conj pending)))

(defn same-or-larger?
  [shortest candidate]
  (and (not (empty? shortest))
       (>= (count candidate) (count shortest))))

(defn next-step
  [pwd prune? new-best [best pending]]
  (if (empty? pending)
    [best nil]
    (let [[pos path] (peek pending) pending (pop pending)]
      (cond
        (grants-vault-access? pos) [(new-best best path) pending]
        (prune? best path) [best pending]
        :else [best (add-moves pending pwd pos path)]))))

(defn walk-around
 [next-step] 
 (iterate next-step ["" (conj empty-queue [[0 0] ""])]))

(defn find-first
  [next-step]
  (->> next-step
       walk-around
       (drop-while (comp not empty? last))
       first
       first))

(defn shortest-path
  [pwd]
  (->> (partial next-step pwd same-or-larger? min-path)
       find-first))

(defn longest-path-length
  [pwd]
  (->> (partial next-step pwd (constantly false) max-path)
       find-first
       count))

(defn print-walk
  [walk]
  (doseq [[shortest moves] walk]
    (println shortest)
    (println (map identity moves))))


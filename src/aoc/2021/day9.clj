(ns aoc.2021.day9
  (:require [clojure.set :as st]))


(def nbrs8
  (for [x [-1 0 -1] y [-1 0 1] :when (not= [0 0] [x y])]
    [x y]))

(def nbrs
  (for [x [-1 0 1] y [-1 0 1]
        :when (and (or (zero? x) (zero? y))
                   (not= [0 0] [x y]))]
    [x y]))


(defn pt-nbrs [pt]
  (map #(map + pt %) nbrs))


(defn all-pts
  [[f :as flows]]
  (for [x (range (count flows)) y (range (count f))]
    [x y]))


(defn low-point?
  [flows pt]
  (println pt)
  (let [tgt (get-in flows pt)]
    (->> nbrs
         (map #(map + pt %))
         (map #(get-in flows % 10))
         (every? (partial < tgt)))))


(defn low-points
  [flows]
  (->> flows
       all-pts
       (filter (partial low-point? flows))))


(defn sum-low-points
  [flows]
  (->> flows
       low-points
       (map #(get-in flows %))
       (map inc)
       (apply +)))


(defn find-basin-pts
  [flows pt]
  (->> pt
       pt-nbrs
       (filter #(not= 9 (get-in flows % 9)))
       set))


(defn find-nbrs
  [flows {:keys [basin pending] :as state}]
  (let [fst (first pending)
        new-pts (find-basin-pts flows fst)
        new-pts (st/difference new-pts basin)]
    (-> state
        (update :basin conj fst)
        (update :pending disj fst)
        (update :pending st/union new-pts))))



(defn basin
  [flows pt]
  (->> {:basin #{} :pending #{pt}}
       (iterate (partial find-nbrs flows))
       (drop-while (comp seq :pending))
       first
       :basin))


(defn find-basins
  [flows]
  (->> flows
       low-points
       (map (partial basin flows))))


(defn find-largest-basins
  [flows]
  (->> flows
       find-basins
       (map count)
       (sort >=)
       (take 3)
       (apply *)))



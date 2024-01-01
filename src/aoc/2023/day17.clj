(ns aoc.2023.day17
  (:require [clojure.data.priority-map :as pm]))

(def dirs {:u [-1 0] :d [1 0]
           :l [0 -1] :r [0 1]})


(defn mk-cursor [pt d steps]
  [pt (cond-> d (keyword? d) (dirs)) steps])


(defn sum-pts [pt1 pt2] (mapv + pt1 pt2))


(defn- advance-cursor [[pt dir total]]
  (mk-cursor (sum-pts pt dir) dir (inc total)))


(defn- rt-left [[pt [x y]]]
  (advance-cursor (mk-cursor pt [(* -1 y) x] -1)))


(defn- rt-right [[pt [x y]]]
  (advance-cursor (mk-cursor pt [y (* -1 x)] -1)))


(defn ultra-adjacent
  "Once an ultra crucible starts moving in a direction, it needs to move a minimum of four blocks in that direction before it can turn (or even before it can stop at the end)"
  [[_ _ total :as cursor]]
  (cond-> []
    (< total 9) (conj (advance-cursor cursor))
    (< 2 total) (conj (rt-left cursor)
                      (rt-right cursor))))

(defn calc-adjacent [[_ _ total :as cursor]]
  (cond-> []
    (< total 2) (conj (advance-cursor cursor))
    :always (conj (rt-left cursor)
                  (rt-right cursor))))

(defn inside? [traffic [[x y]]]
  (and (<= 0 x (dec (count traffic) ))
       (<= 0 y (dec (count (first traffic)) ))))

(defn- visited? [visited? [[pt]]]
  (visited? pt))

(def min-wd (fnil min Integer/MAX_VALUE))

(defn update-distances [distances
                        nodes
                       {:keys [current traffic]}]
  (let [[[pt]] current
        prev-dist (distances pt)]
    (->> nodes
         (reduce (fn [dist [pt2]]
                   (let [new-dist (+ prev-dist (get-in traffic pt2))]
                     (update dist pt2 min-wd new-dist)))
                 distances))))


(defn- add-distance [traffic prev [pt :as cursor]]
  (->> (get-in traffic pt)
       (+ prev)
       (vector cursor)))


(defn unvisited-neighbours
  [traffic [cursor dist] adj-fn]
  (assert cursor)
  (->> cursor
       adj-fn
       (filter (partial inside? traffic))
       (map (partial add-distance traffic dist))
       (into {})))


(defn add-neighbours
  [{:keys [visited target-found? adj-fn] :as state} traffic current]
  (assert current)
  (let [[cursor dist] current
        nodes (unvisited-neighbours traffic current adj-fn)]
    (assert (not (visited cursor)) (format "It is visited %s" cursor))
    (-> state
        (update :found #(or % (target-found? current)))
        (update :visited assoc cursor dist)
        (update :pending (partial merge-with min) nodes))))


(defn path-step
  [traffic {:keys [pending visited] :as state}]
  (assert (not-empty pending))
  (let [[cursor :as current] (first pending)]
    (cond-> state
      (not (visited cursor)) (add-neighbours traffic current)
      :always (update :pending dissoc cursor))))



(defn- match-current [target ]
  (fn [[[pos] :as current]]
    (when (and (= target pos))
      current)))


(defn- match-current-ultra [target]
  (let [f (match-current target)]
    (fn [[[_ _ total] :as current]]
      (when (and (f current) (<= 3 total))
        current))))


(defn- bottom-right-corner [traffic]
  (let [rows (count traffic) cols (count (first traffic))]
    [(dec rows) (dec cols)]))


(defn path-min-heat-loss
  ([traffic] (path-min-heat-loss traffic calc-adjacent match-current))
  ([traffic adj-fn target-fn]
  (let [target-found? (target-fn (bottom-right-corner traffic))]
    (->> {:pending (pm/priority-map
                    (mk-cursor [0 0] :r -1) 0
                    (mk-cursor [0 0] :d -1) 0)
          :visited {}
          :adj-fn adj-fn
          :target-found? target-found?}
         (iterate (partial path-step traffic))
         (drop-while (complement :found))
         first
         :found
         last)) ))



(defn path-min-heat-loss-ultra [traffic]
  (path-min-heat-loss traffic
                      ultra-adjacent
                      match-current-ultra))

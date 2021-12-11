(ns aoc.2021.day11
  (:require [clojure.set :as st]))

(def nbrs-pts
  (for [x [-1 0 1] y [-1 0 1] :when (not= [0 0] [x y])]
    [x y]))


(defn nbrs
  [pt]
  (->> nbrs-pts
       (map #(map + pt %))
       (filter (fn [[x y]] (and (< -1 x 10) (< -1 y 10))))
       set))


(def all-pts
  (for [x (range 10) y (range 10)] [x y]))


(defn inc-level
  ([octopi] (inc-level octopi all-pts))
  ([octopi pts]
   (reduce
    (fn [octopi pt] (update-in octopi pt inc))
    octopi
    pts)))


(def flash? (partial < 9))


(def level get-in)


(defn levels
  [octopi pts]
  (map (partial level octopi) pts))


(defn flash-it
  [{:keys [pending flashed octopi]}]
  (let [flashing (->> pending
                      (filter (comp flash? (partial level octopi)))
                      set)
        flashed (st/union flashed flashing)
        new-pending (mapcat nbrs flashing)
        pending (remove flashed new-pending)]
    {:flashed flashed
     :pending pending
     :octopi (inc-level octopi pending)}))


(defn update-flashing
  [octopi]
  (->> {:pending all-pts :octopi octopi :flashed #{}}
       (iterate flash-it)
       (drop-while (comp seq :pending))
       first
       (#(select-keys % [:flashed :octopi]))))


(defn reset-level
  [{:keys [flashed] :as state}]
  (update state
          :octopi
          #(reduce
            (fn [octopi pt] (assoc-in octopi pt 0))
            %
            flashed)))


(defn octo-step
  [{:keys [octopi]}]
  (->> octopi
       inc-level
       update-flashing
       reset-level))


(def steps (partial iterate octo-step))


(defn count-flashes
  [octopi n]
  (->> {:octopi octopi}
       steps
       (take (inc n))
       (map (comp count :flashed))
       (apply +)))


(defn sync?
  [octopi]
  (->> octopi
       (map set)
       (apply = #{0})))


(defn sync-flash
  [octopi]
  (->> {:octopi octopi}
       steps
       (map :octopi)
       (take-while (complement sync?))
       count))

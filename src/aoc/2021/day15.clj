(ns aoc.2021.day15
  (:require [clojure.data.priority-map :refer [priority-map]]))

(def nbrs-pts
  [[-1 0] [0 -1] [1 0] [0 1]])


(defn nbrs
  [pt]
  (map #(map + pt %) nbrs-pts))


(defn new-nodes
  [pt risk visited? cave]
  (let [end (count cave)
        inside? (fn [[x y]] (and (< -1 x end)
                                (< -1 y end)))]
    (->> pt
         nbrs
         (filter inside?)
         (remove visited?)
         (map #(vector % (+ risk (get-in cave %)))))))


(defn update-min-risk
  [pending new-nodes]
  (reduce
   (fn [m [pt new-risk]]
     (update m
             pt
             #(min (or % ##Inf) new-risk)))
   pending
   new-nodes))


(defn find-shortest-path
  [cave ]
  (let [end (repeat 2 (dec (count cave)))]
    (loop [pending (priority-map [0 0] 0)
           visited? {}]
      (let [[pt risk] (first pending)
            pending (dissoc pending pt)]
        (cond
          (not pt) (visited? end)
          :else (recur
                 (update-min-risk pending
                                  (new-nodes pt risk visited? cave))
                 (assoc visited? pt risk)))))))


(ns aoc.2016.day15
  (:require [clojure.tools.trace :refer [trace]]
            [clojure.string :refer [join starts-with? split]]))


(defn tick
  [discs]
  (doall (map (fn [[m s]] [m (mod (inc s) m)]) discs)))


(defn all-zeroes?
  [dsc]
  (->> dsc
       (map last)
       (every? #(= 0 %))))

(defn aligned?
  [[[_ d] & rst]]
  (and (= d 0) (or (nil? rst) (recur (tick rst)))))

(defn find-time
  [discs]
  (->> discs
       (iterate tick)
       (take-while #(not (aligned? %)))
       count
       dec))

(defn find-time2
  [discs]
  (loop [i 0 discs discs]
    (let [discs (tick discs)]
      (if (aligned? discs)
        i
        (recur (inc i) discs)))))

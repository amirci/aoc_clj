(ns aoc.2017.day6
  (:require [clojure.tools.trace :refer [trace]]))


(defn max-index
  [banks]
  (let [mx (apply max banks)]
    (->> banks
         (map vector (range))
         (drop-while #(-> % second (not= mx)))
         first)))

(def sum-lists (partial map +))

(defn shift
  [n l]
  (->> l
       cycle
       (drop (- (count l) n))
       (take (count l))))

(defn div-blocks
  [b len]
  (let [q (quot b len)
        r (rem b len)]
    (sum-lists
         (repeat len q)
         (concat (repeat r 1) (repeat (- len r) 0)))))

(defn dist-blocks
  [[i b] banks]
  (->> banks
       count
       (div-blocks b)
       (shift (inc i))
       (sum-lists (assoc banks i 0))
       vec))

(defn dist-max [l] (-> l max-index (dist-blocks l)))

(defn new-config
  [{:keys [cfg seen]}]
  (let [new-cfg (dist-max cfg)]
    (if (seen new-cfg)
      {:duplicate new-cfg}
      {:cfg new-cfg :seen (conj seen new-cfg)})))

(defn config-until-duplicate
  [c]
  (->> {:cfg c :seen #{} :duplicate nil}
       (iterate new-config)
       (take-while #(-> % :duplicate nil?))))

(defn first-duplicate
  [c]
  (->> {:cfg c :seen #{} :duplicate nil}
       (iterate new-config)
       (drop-while #(-> % :duplicate nil?))
       first
       :duplicate))

(defn find-first-duplicate-config-length
  [c]
  (-> c
      config-until-duplicate
      count))

(defn find-loop-length
  [c]
  (let [cfs (config-until-duplicate c)
        dup (first-duplicate c)
        skip (take-while #(-> % :cfg (not= dup)) cfs)]
    (- (count cfs) (count skip))))

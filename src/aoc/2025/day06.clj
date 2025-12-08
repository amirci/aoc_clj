(ns aoc.2025.day06
  (:require [clojure.string :as s]))

(defn- parse-ops [line]
  (->> line
       (re-seq #"[\*\+]")
       (map {"*" *' "+" +'})
       ))

(map class
     (parse-ops "+ *"))

(defn solve-cephalopod-math [problems]
  (->> problems
       butlast
       (map (fn [s] (read-string (str "[" s "]"))))
       (apply mapv vector)
       (map #(cons %1 %2) (parse-ops (last problems)))
       (map (fn [[op & nbrs]] (apply op nbrs)))
       (apply +)))


(defn solve-cephalopod-math-correctly [problems]
  (->> problems
       butlast
       (apply map vector)
       (partition-by (partial every? #{\space}))
       (remove #(= 1 (count %)))
       (map (partial map (comp read-string (partial apply str))))
       (map #(cons %1 %2) (parse-ops (last problems)))
       (map (fn [[op & nbrs]] (apply op nbrs)))
       (apply +)))





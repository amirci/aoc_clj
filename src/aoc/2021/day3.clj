(ns aoc.2021.day3
  (:require [clojure.string :as st]))

(defn ->binary
  [num]
  (->> num
       st/join
       (str "2r")
       read-string))


(defn calc-rates
  [report]
  (->> report
       (apply map vector)
       (map frequencies)
       (map (partial sort-by second))
       (map (partial map first))
       (apply map vector)
       (map ->binary)
       (apply *)))


(defn sorted
  [freqs]
  (if (apply = (vals freqs))
    [\0 \1]
    (map first (sort-by second freqs))))


(defn calc-rates-step
  [bit-idx nbrs]
  (->> nbrs
       (apply map vector)
       (map frequencies)
       (#(nth % bit-idx))
       (merge {\1 0 \0 0})
       sorted))


(defn filter-match
  [nbrs bit-idx d]
  (filter #(= d (nth % bit-idx)) nbrs))

(defn filter-step
  [f bit-idx nbrs]
  {:pre [(pos? (count nbrs))]}
  (if (= 1 (count nbrs))
    nbrs
    (->> (calc-rates-step bit-idx nbrs)
         f
         (filter-match nbrs bit-idx))))


(defn calc-life-support
  [nbrs]
  (->> (range (count (first nbrs)))
       (reduce
        (fn [gases bit-idx]
          (map
           #(filter-step %1 bit-idx %2)
           [first last]
           gases))
        [nbrs nbrs])
       (map (comp ->binary first))
       (apply *)))


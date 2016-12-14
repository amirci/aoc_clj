(ns aoc.2016.day14
  (:require [clojure.tools.trace :refer [trace]]
            [clojure.string :refer [includes?]]
            [digest :refer :all]))

(def salt "cuanljph")

(defn hash-gen
  [salt]
  (memoize (fn [i] [i (digest/md5 (str salt i))])))

(defn valid-hash?
  [hgen [i hsh]]
  (when-let [l (last (re-find #"(\w)\1\1" hsh))]
    (let [k5 (apply str (repeat 5 l))
          next-1000 (->> (range (inc i) (+ i 1001)) (map #(hgen %)))
          found (first (filter (fn [[_ hsh]] (includes? hsh k5)) next-1000))]
      (when found [i hsh]))))

(defn next-key
  [hgen i]
  (->> (iterate inc i)
       (map #(hgen %))
       (drop-while (complement (partial valid-hash? hgen)))
       first))

(defn pad-key-gen
  [salt]
  (let [hgen (hash-gen salt)]
    (drop 1 (iterate (fn [[i _]] (next-key hgen (inc i))) [-1]))))





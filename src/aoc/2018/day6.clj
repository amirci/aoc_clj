(ns aoc.2018.day6
  (:require 
    [clojure.string :as str]))


(defn ->map-pts
  [lines]
  (->> lines
       (map #(read-string (str "[[" %"] #{}]")))
       (into {})))

(defn man-dist
  [[a b] [c d]]
  (+
    (java.lang.Math/abs (- a c))
    (java.lang.Math/abs (- b d))))

(defn min-dist
  [pt pts]
  (let [[c1 c2] (->> pts
                    (map #(vector (man-dist pt %) %))
                    sort
                    (take 2))]

    (when (not= (first c1) (first c2))
      (second c1))))

(defn assign-closest
  [m pt]
  (let [closest (min-dist pt (keys m))]
    (if closest
      (update m closest conj pt)
      m)))

(defn calc-areas
  [pm [min-x min-y max-x max-y]]
  (->> (for [x (range min-x max-x) y (range min-y max-y)] [x y])
       (reduce assign-closest pm)
       (map (fn [[k v]] [k (count v)]))))

(defn remove-infinite
  [areas [min-x min-y max-x max-y]]
  (let [[min-x min-y max-x max-y] [(dec min-x) (dec min-y) (inc max-x) (inc max-y)]]

    )
  (filter (set as2) as1))

(defn calc-boundaries
  [input]
  (reduce
    (fn [[min-x min-y max-x max-y] [x y]]
      [(min min-x x)
       (min min-y y)
       (max max-x x)
       (max max-y y)])
    [1000 1000 0 0]
    (keys input)))

(defn inc-boundaries
  [[a b c d]
   [(dec a) (dec b) (inc a) (inc b)]])

(defn part-a
  [input]
  (let [boundaries (calc-boundaries input)
        areas (calc-areas input boundaries)]
    (->> (remove-infinite areas boundaries)
         (apply max-key second)
         second)))



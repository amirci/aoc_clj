(ns aoc.2018.day6
  (:require 
    [clojure.string :as str]))


(defn ->map-pts
  [lines]
  (->> lines
       (map #(read-string (str "[[" %"] #{}]")))
       (into {})))

(defn man-dist*
  [[a b] [c d]]
  (+
    (java.lang.Math/abs (- a c))
    (java.lang.Math/abs (- b d))))

(def man-dist (memoize man-dist*))


(defn all-points-in 
  [[min-x min-y max-x max-y]]
  (for [x (range min-x (inc max-x)) y (range min-y (inc max-y))] [x y]))

(defn- choose-closest
  [[[d1 p1] [d2 p2]]]
  (if (= d1 d2)
    nil
    p1))

(defn closest-point
  [pts query-pt]
  (->> pts
       (map #(vector (man-dist query-pt %) %))
       sort
       (take 2)
       choose-closest))

(defn calc-areas
  [points boundaries]
  (reduce
    (fn [areas query-pt]
      (if-let [closest (closest-point (keys areas) query-pt)]
        (update areas closest conj query-pt)
        areas))
    (->> points (map #(vector % #{})) (into {}))
    (all-points-in boundaries)))

(defn increase-boundaries
  [[min-x min-y max-x max-y]]
  (let [[min-x min-y max-x max-y] [(dec min-x) (dec min-y) (inc max-x) (inc max-y)]]
    (set
      (concat
        (for [y (range min-y (inc max-y))] [min-x y])
        (for [y (range min-y (inc max-y))] [max-x y])
        (for [x (range min-x (inc max-x))] [x min-y])
        (for [x (range min-x (inc max-x))] [x max-y])))))

(defn remove-infinite
  "Make boundaries 'grow' and see which areas change"
  [areas bdrs]
  (let [new-pts (increase-boundaries bdrs)
        all-pts (keys areas)]
    (reduce
      (fn [fin-areas pt]
        (if-let [closest (closest-point all-pts pt)]
          (dissoc fin-areas closest)
          fin-areas ))
      areas
      new-pts)))

(defn calc-boundaries
  [points]
  (reduce
    (fn [[min-x min-y max-x max-y] [x y]]
      [(min min-x x)
       (min min-y y)
       (max max-x x)
       (max max-y y)])
    [1000 1000 0 0]
    points))

(defn part-a
  [input]
  (let [boundaries (calc-boundaries input)
        areas (calc-areas input boundaries)]
    (->> (remove-infinite areas boundaries)
         (map (comp count second))
         (apply max)
         )))



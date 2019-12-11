(ns aoc.2019.day3
  (:require [clojure.edn :as edn]))

(def sum-points (partial map +))

(def dir->delta
  {\R [0 1]
   \L [0 -1]
   \U [1 0]
   \D [-1 0]})

(defn dir->pt
  [dir]
  (let [nbr (edn/read-string (subs dir 1))
        delta (dir->delta (first dir))]
    (map * delta (repeat nbr))))

(defn walking-steps
  [dir]
  (let [nbr (edn/read-string (subs dir 1))
        delta (dir->delta (first dir))]
    #(->> % 
          (iterate (partial sum-points delta))
          (drop 1)
          (take nbr))))


(defn walk-to
  [starting-point location]
  ((walking-steps location) starting-point))

(defn path-points
  [path]
  (reduce (fn [coll to]
            (let [from (or (last coll) [0 0])
                  points (walk-to from to)]
              (concat coll points)))
          []
          path))

(defn mhd
  [pt]
  (->> pt
       (map #(java.lang.Math/abs %))
       (apply +)))

(defn closest-intersection
  [wire1-path wire2-path]
  (->> [wire1-path wire2-path]
       (map (comp set path-points))
       (apply clojure.set/intersection)
       (map mhd)
       (apply min)))

(defn steps-until
  [p coll]
  (->> coll
       (take-while (partial not= p))
       count
       inc))

(defn fewest-combined-steps
  [wire1-path wire2-path]
  (let [[pts1 pts2] (map path-points [wire1-path wire2-path])
        intersect (clojure.set/intersection (set pts1) (set pts2))]
    (apply min
           (for [p intersect]
               (->> [pts1 pts2]
                    (map (partial steps-until p))
                    (apply +))))))


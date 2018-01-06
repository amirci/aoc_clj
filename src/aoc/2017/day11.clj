(ns aoc.2017.day11
  (:require [clojure.tools.trace :refer [trace]]))

(defn dir->point
  [dir]
  (case dir
    "ne" [1 -1]
    "n"  [0 -1]
    "nw" [-1 0]
    "se" [1 0]
    "s"  [0 1]
    "sw" [-1 1]))

(defn move
  [[x y] dir]
  (->> dir
       dir->point
       (map + [x y])))


(defn follow-path
  [dirs]
  (reduce move [0 0] dirs))


(defn hex-distance
  ([[x y]] (hex-distance [x y] [0 0]))
  ([[x y] [a b]]
   (/ (+ (Math/abs (- y b))
         (Math/abs (- (+ x y) (+ a b)))
         (Math/abs (- x a)))
      2)))

(def path-distance (comp hex-distance follow-path))


(defn max-path-distance 
  [input]
  (->> input
       (reductions move [0 0])
       (map hex-distance)
       (apply max)))

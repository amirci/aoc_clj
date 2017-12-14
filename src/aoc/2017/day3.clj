(ns aoc.2017.day3
  (:require [clojure.tools.trace :refer [trace]]))

(defn square-bounds
  [n]
  (let [isq (int (Math/sqrt n))
        r   (if (odd? isq) 0 1)
        lower (- isq r)]
    [lower (+ 2 lower)]))

(def square #(* % %))

(defn path-to-center-spiral
  [n]
  (let [[lb ub :as bounds] (square-bounds n)
        [slb sub] (map square bounds)
        diff-ub (- sub n)
        side (/ (- sub slb) 4)
        halfway (/ side 2)
        rem-side (rem diff-ub side)
        to-center (+ halfway (- halfway rem-side))]
    (int to-center)))

(defn vplus
  [[a b] [c d]]
  [(+ a c) (+ b d)])

(defn neighbours
  [v]
  (for [x [-1 0 1] y [-1 0 1] :when (not= [x y] [0 0])]
    (vplus v [x y])))

(defn square-range
  [sn]
  (let [n (+ 1 (* 2 sn))
        nsn (* -1 sn)
        [x y] [0 (dec sn)]]
    ;[0 1] [-1 2] [-2 3]
    (concat
      (for [i (range 1 n)] [(+ nsn i) sn])  ; up
      (for [i (range 1 n)] [sn (- sn i)])   ; left
      (for [i (range 1 n)] [(- sn i) nsn])  ; down
      (for [i (range 1 n)] [nsn (+ nsn i)])))) ; right

(defn sum-neighbours
  [m c]
  (->> c
       neighbours
       (map m)
       (remove nil?)
       (apply +)))

(defn find-greater
  [n m c]
  (let [v (sum-neighbours m c)]
    (if (> v n)
      (reduced v)
      (assoc m c v))))

(defn update-square
  [n m sq]
  (reduce (partial find-greater n) m sq))

(defn find-larger-in-spiral-b
  [n]
  (->> (range)
       (drop 1)
       (map square-range)
       (reduce #(let [v (update-square n %1 %2)]
                  (if (map? v) v (reduced v)))
                  {[0 0] 1})))


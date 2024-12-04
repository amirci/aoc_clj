(ns aoc.2024.day4
  (:require [clojure.math :as math]))

(defn word-path [word]
  (for [i (range (count word))] [0 i]))

(defn- add-pt [p1 p2]
  (map + p1 p2))

(def matrix-45
  [[0.7071 -0.7071][0.7071 0.7071]])

(defn rotate-pt [[[a b] [c d]] [x y]]
  [(+ (* y c) (* x a)) (+ (* x b) (* y d))])

(def rt-pt-45 (partial rotate-pt matrix-45))

(defn xx [a]
  ((if (pos? a) math/ceil math/floor) a))

(defn- adjust-pts [pts]
  (map (partial map (comp int xx)) pts))

(defn all-paths [path]
  (->> path
       (iterate (partial map rt-pt-45))
       (take 8)
       (map adjust-pts)))

(defn follow-word [board start path]
  (map
   #(get-in board (add-pt start %))
   path))

(defn matching-words [board paths word start]
  (->> paths
       (filter #(= word (follow-word board start %)))
       count))

(defn find-xmas [board]
  (let [word "XMAS"
        paths (all-paths (word-path word))]

    (->> (for [x (range 0 (count board))
               y (range 0 (count (first board)))]
           [x y])
         (filter #(= (first word) (get-in board %)))
         (map (partial matching-words board paths (seq word)))
         (apply +))))

(def x-path [[-1 -1] [1 1] [-1 1] [1 -1]])

(def target-x (->> ["MSMS" "MSSM" "SMSM" "SMMS"]
                   (map seq)
                   set))

(defn x-mas? [board pt]
  (target-x (follow-word board pt x-path)))

(defn find-x-mas [board]
  (->> (for [x (range 1 (dec (count board)))
             y (range 1 (dec (count (first board))))]
         [x y])
       (filter #(= \A (get-in board %)))
       (filter (partial x-mas? board))
       count))

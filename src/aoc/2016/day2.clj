(ns aoc.2016.day2
  (:require [clojure.tools.trace :refer [trace]]))

(def keypad
  (into {}
        (for [x (range 3) y (range 3)] [(inc (+ (* x 3) y)) [x y]])))

(def nbr->point keypad)

(def point->nbr (clojure.set/map-invert keypad))

(def move->point {\L [0 -1] \R [0 1] \D [1 0] \U [-1 0]})

(defn move->nbr
  [nbr move]
  (let [mv-pt (move->point move)]
    (->> (or nbr 5)
         nbr->point
         (map + mv-pt)
         point->nbr
         (#(or % nbr)))))

(defn moves->nbr
  [nbrs moves]
  (->> moves
       (reduce move->nbr (last nbrs))
       (conj nbrs)))

(defn bathroom-code
  [lines]
  (->> lines
       (reduce moves->nbr [])
       (apply str)))

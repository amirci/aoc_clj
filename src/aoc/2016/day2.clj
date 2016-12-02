(ns aoc.2016.day2
  (:require [clojure.tools.trace :refer [trace]]))

(def keypad
  (into {}
        (for [x (range 3) y (range 3)] [(inc (+ (* x 3) y)) [x y]])))

(def keypad2
  {1 [0 2]
   2 [1 1] 3 [1 2] 4 [1 3]
   5 [2 0] 6 [2 1] 7 [2 2] 8 [2 3] 9 [2 4]
   \A [3 1] \B [3 2] \C [3 3]
   \D [4 2]})

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

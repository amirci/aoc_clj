(ns aoc.2018.day9
  (:require 
    [clojure.string :as str]))

(defn next-player
  [p players]
  (if (= p (count players)) 1 (inc p)))

(defn next-index
  [i marbles]
  (if (= i (dec (count marbles)))
    1
    (+ i 2)))

(def calc-next-marble inc)

(defn insert-marble
  [circle current marble]
  (.add circle current marble)
  circle)

(defn empty-marble-circle [] (insert-marble (java.util.ArrayList.) 0 0))

(defn init-game
  [total-players total-marbles]
  {:player 1
   :scores (->> (range 1 (inc total-players)) (mapv #(vector % 0)) (into {}))
   :current 0
   :total total-marbles
   :marble-circle (empty-marble-circle) 
   :next-marble 1})

(defn play-turn
  [{:keys [player scores total next-marble marble-circle current] :as game}]
  (let [current (next-index current marble-circle)]
    (-> game
        (update :marble-circle insert-marble current next-marble)
        (assoc  :current       current)
        (update :player        next-player scores)
        (update :next-marble   calc-next-marble))))


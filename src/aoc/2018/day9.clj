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

(defn remove-marble
  [^java.util.ArrayList circle pos]
  (.remove circle (int pos))
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

(defn find-prize
  "Finds ccw 7 position"
  [current circle]
  (let [current (- current 7)]
    (if (neg? current)
      (- (count circle) (java.lang.Math/abs current))
      current)))

(defn find-after-prize
  [prize circle]
  (mod prize (dec (count circle))))

(defn play-turn-23
  [{:keys [player scores total next-marble marble-circle current] :as game}]
  (let [prize (find-prize current marble-circle)]
    (-> game
        (update :scores        update player + next-marble prize)
        (update :marble-circle remove-marble prize)
        (assoc  :current       (find-after-prize prize marble-circle)))))

(defn play-turn-regular
  [{:keys [next-marble marble-circle current] :as game}]
  (let [current (next-index current marble-circle)]
    (-> game
        (update :marble-circle insert-marble current next-marble)
        (assoc  :current       current))))

(defn play-turn
  [{:keys [next-marble scores] :as game}]
  (let [f (if (zero? (mod next-marble 23)) play-turn-23 play-turn-regular)]
    (-> game
        f
        (update :player        next-player scores)
        (update :next-marble   calc-next-marble))))

(ns aoc.2019.day13
  (:require
   [clojure.core.async :as a]
   [taoensso.timbre :as log]
   [aoc.2019.day5 :as day5]))

(defn ->tile
  [n]
  (assert n "TILE Can't be nil!")
  (assert (<= 0 n 4) (str "TILE " n " should be 0 <= n <= 4" ))
  (nth [:empty :wall :block :paddle :ball] n))

(def score? (partial = [-1 0]))

(def ball? (partial = 4))
(def paddle? (partial = 3))
(def other? (partial > 3))

(defn build-tiles
  [m [x y tile-id]]
  (when (ball? tile-id)
    (log/debug "Move ball to" x y))
  (when (paddle? tile-id)
    (log/debug "Move paddle to" x y)) 
  (cond-> m
    (ball?   tile-id) (assoc  :ball   [x y])
    (paddle? tile-id) (assoc  :paddle [x y])
    (score?  [x y])   (assoc  :score  tile-id)
    (other?  tile-id) (update :board  assoc [x y] (->tile tile-id))))

(defn play-game-demo
  [game]
  (->> game
       day5/run-program
       :outputs
       (partition-all 3)
       (reduce build-tiles {})))

(defn joystik-input
  [{:keys [game] :as program}]
  (let [[px py] (:paddle game)
        [bx by] (:ball game)
        move (compare bx px)]
    (log/debug "Joystik" move "(Paddle" px py "vs ball" bx by ")")
    (a/<!! (a/timeout 1000))
    move))

(defn run-tile-instruction
  [{:keys [game] :as program}]
  (let [triplet (:output game)]
    (-> program
        (update :game build-tiles triplet)
        (assoc-in [:game :output] []))))

(defn tile-instruction?
  [game]
  (= 3 (count (:output game))))

(defn update-board
  [{:keys [game] :as program}]
  (if (tile-instruction? game)
    (run-tile-instruction program)
    program))

(defn update-game-from-output
  [program val]
  (-> program
      (update-in [:game :output] conj val)
      update-board))

(def new-game
  {:board {} :ball nil :paddle nil :output []})

(defn play-game-to-destroy-all-tiles
  ([game] (play-game-to-destroy-all-tiles game
                                          joystik-input
                                          update-game-from-output))
  ([game input-fn output-fn]
   (-> game
       (assoc 0 2)
       (day5/->instruction input-fn output-fn)
       (assoc :game new-game)
       day5/run-program*))))

(defn count-block-tiles
  [code]
  (->> code
       run-game
       (filter (comp (partial = :block) second))
       count))

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

(def block? (comp (partial = :block) second))

(defn count-block-tiles
  [{:keys [board] :as game}]
  (->> board
       (filter block?)
       count))


(defn joystik-input
  [{:keys [game] :as program}]
  (let [[px py] (:paddle game)
        [bx by] (:ball game)
        move (compare bx px)]
    (log/debug "Joystik" move "(Paddle" px py "vs ball" bx by ")")
    (a/<!! (a/timeout 1000))
    move))

(defn parse-tile-instruction
  [{:keys [game] :as program}]
  (log/debug "Total blocks" (count-block-tiles game))
  (let [triplet (:output game)]
    (-> program
        (update :game build-tiles triplet)
        (assoc-in [:game :output] []))))

(defn ready-tile?
  [game]
  (= 3 (count (:output game))))

(defn update-board
  [{:keys [game] :as program}]
  (cond-> program
    (ready-tile? game) parse-tile-instruction))

(defn update-game-from-output
  [program val]
  (-> program
      (update-in [:game :output] conj val)
      update-board))

(def new-game
  {:board {} :ball nil :paddle nil :output []})

(def play-for-free #(assoc % 0 2))

(defn setup-new-game
  [game input-fn output-fn]
  (-> game
      (day5/->instruction input-fn output-fn)
      (assoc :game new-game)))

(defn play-game-to-destroy-all-tiles
  ([game] (play-game-to-destroy-all-tiles game
                                          joystik-input
                                          update-game-from-output))
  ([game input-fn output-fn]
   (-> game
       play-for-free
       (setup-new-game input-fn output-fn)
       day5/run-program*)))


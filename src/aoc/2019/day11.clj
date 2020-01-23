(ns aoc.2019.day11
  (:require
   [clojure.core.async :as async]
   [taoensso.timbre :as log]
   [aoc.2019.day5 :as day5]))

(def ->color {1 :white 0 :black})

(def color-> {:white 1 :black 0})

(def up [0 -1])

(def turn-left  [-1 1])
(def turn-right [1 -1])

(def ->rotation {0 turn-left 1 turn-right})

(defn rotate
  [[x y] [xf yf]]
  [(* yf y) (* x xf)])

(defn move
  [pt dir]
  (map + pt dir))

(defn paint-and-move
  [{:keys [position dir painted] :as robot} [color rotation]]
  (let [new-dir   (rotate dir rotation)
        new-pos   (move position new-dir)]
    (-> robot
        (assoc  :dir new-dir)
        (assoc  :position new-pos)
        (update :painted assoc position color))))

(defn robot-color
  [{:keys [position painted] :as robot}]
  (get painted position :black))

(defn ->move
  [pair]
  (map apply [->color ->rotation] pair [nil nil]))

(defn paint-loop
  [color-ch moves-ch robot]
  (async/reduce (fn [robot new-move]
                  (let [new-robot (paint-and-move robot new-move)]
                    (async/>!! color-ch (robot-color new-robot))
                    new-robot))
                robot
                moves-ch))

(def init-robot {:position [0 0] :dir up :painted {}})

(defn mk-moves-ch
  []
  (async/chan 2
              (comp (partition-all 2) (map ->move))
              (fn [e]
                (log/debug "EXCEPTION!")
                (log/debug e)
                :exception)))

(defn paint-hull-robot
  ([program] (paint-hull-robot  program init-robot))
  ([program robot]
  (let [color-ch (async/chan 2 (map color->))
        moves-ch (mk-moves-ch)
        results-ch (paint-loop color-ch moves-ch robot)]

    (async/go (async/>! color-ch (robot-color robot)))

    (day5/run-program program
                      (fn [prg]
                        (let [input (async/<!! color-ch)]
                          (assert input "INPUT CANT BE NIL")
                          input))
                      (fn [prg value]
                        (async/>!! moves-ch value)
                        prg))

    (async/close! moves-ch)
    (async/close! color-ch)

    (async/<!! results-ch))))

(defn total-distinct-painted
  [program]
  (-> program
      paint-hull-robot
      :painted
      count))

(def init-robot-white
  (update init-robot :painted assoc [0 0] :white))

(defn paint-license
  [program]
  (-> program
      (paint-hull-robot init-robot-white)
      :painted))

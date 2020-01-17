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

(defn write-output-async
  [ch program value]
  (log/debug "--- OUTPUT" value)
  (async/go (async/>! ch value))
  program)

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
                    (async/go
                      (async/>! color-ch (robot-color new-robot)))
                    new-robot))
                robot
                moves-ch))

(def init-robot {:position [0 0] :dir up :painted {}})

(defn mk-moves-ch
  []
  (async/chan 1
              (comp (partition-all 2) (map ->move))
              (fn [e]
                (log/debug "EXCEPTION!")
                (log/debug e)
                :exception)))

(defn paint-hull-robot
  [program]
  (let [robot init-robot
        color-ch (async/chan 1 (map color->))
        moves-ch (mk-moves-ch)
        results-ch (paint-loop color-ch moves-ch robot)]

    (async/go (async/>! color-ch (robot-color robot)))

    (day5/run-program program
                      (fn [_]
                        (let [input (async/<!! color-ch)]
                          (log/debug ">>> INPUT" input)
                          input))
                      (partial write-output-async moves-ch))

    (async/close! moves-ch)
    (async/close! color-ch)

    (async/<!! results-ch)))

(defn total-distinct-painted
  [program]
  (-> program
      paint-hull-robot
      :painted
      count))


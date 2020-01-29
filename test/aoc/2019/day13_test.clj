(ns aoc.2019.day13-test
  (:require [aoc.2019.day13 :as sut]
            [clojure.core.async :as a]
            [clojure.data :as cljd]
            [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.test :refer [deftest testing is]]
            [clojure.test.check.generators :as g]
            [com.gfredericks.test.chuck :as tc]
            [com.gfredericks.test.chuck.clojure-test :refer [checking]]
            [quil.core :as q]
            [quil.middleware :as m]
            [taoensso.timbre :as log]))

(def input (io/resource "2019/day13.input.txt"))

(def intcode
  (->> input
       slurp
       clojure.string/split-lines
       first
       (format "[%s]")
       edn/read-string))

(deftest part-a-test
  (is (= 226
         (sut/count-block-tiles intcode))))

(def factor 20)

(def width factor)

(def half (/ width 2))
(def qtr  (/ width 4))

(defn setup
  [input-fn output-fn]
  (q/frame-rate 4)
  (q/stroke 0)
  (q/text "Loading..." 800 100)
  (q/no-loop)
  {:board (sut/run-game-for-free intcode input-fn output-fn)})

(defn draw
  [{:keys [board]}]
  (q/background 255)
  (q/fill 100 200 255)
  (q/text (str "Score: " (:score board)) 800 100)
  (doseq [[[i j] tile] (dissoc board :score)]
    (q/fill 220 200 255)
    (q/stroke 0 0 0)
    (let [[x y] (map #(+ (* % factor) factor) [i j])]
      (case tile
        :wall (do
                (q/fill 96 214 73)
                (q/rect x y width width))

        :block (do
                 (q/fill 255 200 255)
                 (q/rect x y width width))

        :h-paddle (do
                    (q/fill 255 0 0)
                    (q/rect x (+ y qtr) width half)
                    #_(q/line x (+ y half) (+ x width) (+ y half)))

        :ball (do
                (q/fill 73 129 214)
                (q/ellipse (+ x half) (+ y half) half half))

        (do
          (q/fill 255 255 255)
          (q/rect x y width width))))))

(defn sketch
  []
  (q/defsketch game
    :host "host"
    :setup setup
    :size [1000 1000]
    :draw draw
    :middleware [m/fun-mode] ))

(defn send-1-every-x-seconds
  [program]
  (log/debug "INPUT ASKED!")
  (a/<!! (a/timeout 500))
  (log/debug "INPUT! Sending 0")
  0)

(defn collect-to
  [ch program val]
  (a/>!! ch val)
  program)

(defn update-board
  [board ch game]
  (let [triplet (a/<!! ch)]
    (swap! board sut/build-tiles triplet)
    (assoc game :board @board)))

(defn setup-board
  []
  (q/frame-rate 32)
  {:board {}})

(defn sketch-play-game
  []
  (let [board (atom {})
        input-fn send-1-every-x-seconds
        output-ch (a/chan 3 (partition-all 3))
        output-fn (partial collect-to output-ch)]

    (q/defsketch game2
      :host "host"
      :setup setup-board
      :size [1000 1000]
      :draw draw
      :update (partial update-board board output-ch)
      :middleware [m/fun-mode])

    (sut/run-game-for-free intcode input-fn output-fn)
    (a/close! output-ch)
    ))

(comment

  (sketch)

  (.redraw game)

  (sketch-play-game)

  (println "\n\n----------- START ---------\n\n")
  )

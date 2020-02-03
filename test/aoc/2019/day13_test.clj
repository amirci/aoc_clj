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
         (sut/count-block-tiles
           (sut/play-game-demo intcode)))))

(def factor 20)

(def width factor)

(def half (/ width 2))
(def qtr  (/ width 4))

(defn setup
  [input-fn output-fn]
  (q/frame-rate 4)
  (q/stroke 0)
  (q/text "Loading... please wait" 800 100)
  (q/no-loop)
  {:board (sut/play-game-demo intcode)})

(defn draw
  [{:keys [board]}]
  (q/background 255)
  (q/fill 0 0 0)
  (q/text (str "Score: " (:score board)) 800 150)
  (if (< (count board) 800)
    (do
      (q/fill 255 0 0)
      (q/text (format "Loading %.2f %% please wait..." (* 100 (float (/ (count board) 800) ))) 800 100))

    (do
      (doseq [[[i j] tile] (dissoc board :score :ball :paddle)]
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

            (do
              (q/fill 255 255 255)
              (q/rect x y width width)))))

      (when-let [[i j] (:paddle board)]
          (let [[x y] (map #(+ (* % factor) factor) [i j])]
            #_(log/debug "Drawing paddle at" x y)
            (q/fill 255 0 0)
            (q/rect x (+ y qtr) width half)))

      (when-let [[i j] (:ball board)]
          (let [[x y] (map #(+ (* % factor) factor) [i j])]
            #_(log/debug "Drawing ball at" x y)
            (q/fill 73 129 214)
            (q/ellipse (+ x half) (+ y half) half half)))

      )))

(defn sketch
  []
  (q/defsketch game
    :host "host"
    :setup setup
    :size [1000 1000]
    :draw draw
    :middleware [m/fun-mode] ))

(defn joystik-input
  [game program]
  (let [board @game
        [px py] (:paddle board)
        [bx by] (:ball board)
        move (compare bx px)]
    (log/debug "Joystik" move "(Paddle" px py "vs ball" bx by ")")
    (a/<!! (a/timeout 1000))
    move))

(defn collect-to
  [ch program val]
  (a/>!! ch val)
  program)

(defn update-board
  [board ch game]
  (let [triplet (a/<!! ch)]
    (if triplet
      (do
        (swap! board sut/build-tiles triplet)
        (assoc game :board @board))
      game)))

(defn setup-board
  []
  (q/frame-rate 30)
  {:board {}})

(defn sketch-play-game
  []
  (let [board (atom {})
        input-fn (partial joystik-input board)
        output-ch (a/chan 3 (partition-all 3))
        output-fn (partial collect-to output-ch)]

    (q/defsketch game2
      :host "host"
      :setup setup-board
      :size [1000 1000]
      :draw draw
      :update (partial update-board board output-ch)
      :middleware [m/fun-mode])

    (sut/play-game-to-destroy-all-tiles intcode input-fn output-fn)
    (a/close! output-ch)

    (q/with-sketch (q/get-sketch-by-id game2)
      (q/no-loop))

    (log/info "Game finished!" (:score @board))))


(comment

  (sut/play-game-to-destroy-all-tiles intcode)

  (sketch)

  (.redraw game)

  (sketch-play-game)

  (println "\n\n----------- START ---------\n\n")
  )

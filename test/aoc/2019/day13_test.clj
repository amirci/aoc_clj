(ns aoc.2019.day13-test
  (:require [aoc.2019.day13 :as sut]
            [clojure.core.async :as async]
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

(defn setup []
  (q/frame-rate 4)
  (q/stroke 0)
  (q/text "Loading..." 800 100)
  (q/no-loop)
  {:pending (sut/run-game intcode)})


(defn draw
  [{:keys [pending]}]
  (q/background 255)
  (q/fill 100 200 255)
  #_(q/text (str "Asteroid: " (first pending)) 800 100)
  #_(q/text (str "Total: " total) 800 120)
  (doseq [[[i j] tile] pending
          #_(->> (concat
                 (for [i (range 20)] [[0 i] :wall])
                 (for [i (range 20)] [[1 i] :block])
                 (for [i (range 20)] [[2 i] :h-paddle])
                 (for [i (range 20)] [[3 i] :ball])
                 (for [i (range 20)] [[4 i] :empty])
)
               (into {}))
          ]
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

(comment

  (sketch)

  (.redraw game)
  )

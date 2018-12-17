(ns aoc.2018.day17-test
  (:require [clojure.test :refer :all]
            [aoc.2018.day17 :as dut]))

(defn parse-clay
  [s]
  (-> s
      (clojure.string/replace "x=" ":x ")
      (clojure.string/replace "y=" ":y ")
      (clojure.string/replace #"[,\.]" " ")
      (as-> s (str "[" s "]"))
      read-string
      (->>
        (partition 5))))

(def input
  (-> "resources/2018/day17.input.txt"
      slurp
      parse-clay))

(def sample-input
  (-> "x=495, y=2..7
      y=7, x=495..501
      x=501, y=3..7
      x=498, y=2..4
      x=506, y=1..2
      x=498, y=10..13
      x=504, y=10..13
      y=13, x=498..504"
      parse-clay))

(defn print-clay
  [input]
  (let [clay (dut/calc-clay input)
        [min-x min-y max-x max-y :as bb] (dut/calc-boundaries clay)]
    (println bb)
    (doseq [y (range min-y (inc max-y))]
      (doseq [x (range min-x (inc max-x))]
        (if (clay [x y])
          (print "#")
          (print ".")))
      (println))))

;*e
;(vec (filter #(clojure.string/includes? % "93") (pp2 input)))


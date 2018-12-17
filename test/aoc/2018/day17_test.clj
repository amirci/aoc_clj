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
  [clay water]
  (let [[min-x min-y max-x max-y :as bb] (dut/calc-boundaries clay)]
    (doseq [y (range 0 (inc max-y))]
      (doseq [x (range min-x (inc max-x))]
        (cond
          (clay [x y])  (print "#")
          (water [x y]) (print "|")
          :else         (print ".")))
      (println))))

(def sample-input-clay (dut/calc-clay sample-input))

(->> [sample-input-clay #{} [[500 1]]]
     (iterate dut/flood2)
     (drop 10)
     first
     rest
     )


     (take 10)
     (map last)
     (map println))

(print-clay
  sample-input-clay
  (->> [sample-input-clay #{} [[500 1]]]
       (iterate dut/flood2)
       (drop 34)
       first
       second))

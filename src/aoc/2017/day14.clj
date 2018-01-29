(ns aoc.2017.day14
  (:require
    [blancas.kern.core :refer :all]
    [blancas.kern.lexer.basic :refer :all]
    [clojure.tools.trace :refer [trace]]
    [aoc.2017.day10 :refer [knot]]))

(defn to-squares
  [h]
  (as-> (str "0x" h) h
    (read-string h)
    (Integer/toString h 2)
    (format "%4s" h)
    (clojure.string/replace h #"[\ 0]" ".")
    (clojure.string/replace h #"1" "#")))

(defn count-digit
  [h]
  (as-> (str "0x" h) h
    (read-string h)
    (Integer/toString h 2)
    (filter (partial = \1) h)
    (count h)))

(defn squares-row
  [input]
  (->> input
       knot
       (map to-squares)
       clojure.string/join))

(defn count-row
  [input]
  (->> input
       knot
       (map count-digit)
       (apply +)))

(defn count-squares
  [input]
  (->> (range 128)
       (map #(str input "-" %))
       (map count-row)
       (apply +)))

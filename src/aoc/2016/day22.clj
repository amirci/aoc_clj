(ns aoc.2016.day22
  (:require [clojure.tools.trace :refer [trace]]
            [clojure.math.combinatorics :as combo]
            [clojure.string :refer [split join index-of ]]))


(defn mk-node
  [[x y size used avail per]]
   [[x y]
    {:size size :used used :avail avail :per per}])

(defn load-node
  [node-df]
  (->> node-df
       (re-seq #"\d+")
       (map #(Integer. %))
       mk-node))

(defn load-nodes
  [df]
  (->> df
       (map load-node)
       (into {})))

(defn viable-pair?
  [[{:keys [used]} {:keys [avail]}]]
  (and (> used 0) (>= avail used)))

(defn viable-nodes
  [df]
  (let [nodes (load-nodes df)]
    (->> (for [x (range 30) y (range 32)
               a (range 30) b (range 32) :when (not= [x y] [a b])]
           [(nodes [x y]) (nodes [a b])])
         (filter viable-pair?)
         count)))


(def shortest-moves
  ; after using print-nodes
  ; row 22 col 11 is blank
  ; 4 moves to the left to avoid wall
  ; 22 up
  ; 28 - 7 moves to get before G
  ; 29 * 5 to rotate around the blank to reach column 1
  ; 1 more move for column 0
  (+ 4 22 21 (* 28 5) 1))

(defn print-row
  [row nodes]
  (print (format "%2d " row))
  (doseq [x (range 30)]
    (let [{:keys [size used avail per]} (nodes [x row])]
      (cond
        (= [0 0] [row x]) (print "I")
        (= [0 29] [row x]) (print "G")
        (> used 92) (print "#")
        (= 0 used) (print "_")
        :else (print "."))))
  (println))

(defn print-nodes
  [df]
  (let [nodes (load-nodes df)]
    (print "   ")
    (doseq [x (range 30)] (print (mod x 10)))
    (println)
    (doseq [y (range 32)]
      (print-row y nodes))))

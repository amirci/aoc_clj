(ns aoc.2024.day7
  (:require [clojure.math.combinatorics :as combo]))

(defn quot-mod [n]
  [(quot n 10) (mod n 10)])

(defn ->digits [n]
  (->> [n 0]
       (iterate (comp quot-mod first))
       (drop 1)
       (take-while (complement (partial = [0 0])))
       (map second)
       (into ())))


(defn op-concat [n1 n2]
  (->> n2
       ->digits
       (reduce (fn [acc n] (+ (* acc 10) n)) n1)))


(defn- solved? [test-val [fst & nbrs] ops]
  (->> ops
       (map vector nbrs)
       (reduce (fn [acc [nbr op]]
                 (let [acc (op acc nbr)]
                   (if (<= acc test-val) acc (reduced 0))))
               fst)
       (= test-val)))


(def plus-mult [* +])

(defn- can-be-solved? [ops [test-val & nbrs]]
  (->> (count nbrs)
       dec
       (combo/selections ops)
       (some (partial solved? test-val nbrs))))


(defn total-calibration [equations]
  (->> equations
       (filter (partial can-be-solved? plus-mult))
       (map first)
       (apply +)))


(def plus-mult-concat (conj plus-mult op-concat))


(def solved-or-fixed?
  (some-fn (partial can-be-solved? plus-mult)
           (partial can-be-solved? plus-mult-concat)))


(defn- can-be-solved-or-fixed? [equation]
  (solved-or-fixed? equation))


(defn total-calibration-with-concat [equations]
  (->> equations
       (filter can-be-solved-or-fixed?)
       (map first)
       (apply +)))

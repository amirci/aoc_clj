(ns aoc.2021.day13
  (:require [blancas.kern.core :as kc :refer [<*> >> <$>]]
            [blancas.kern.lexer.basic :as lex]))

(def coord-parser
  (lex/comma-sep kc/dec-num))


(defn fold-y
  [n [x y]]
  [x (- n (- y n))])


(defn fold-x
  [n [x y]]
  [(- n (- x n)) y])


(def fold-parser
  (<*>
   (<$>
    {\y [second fold-y] \x [first fold-x]}
    (>> (kc/token* "fold along ") kc/letter))
   (>> (kc/token* "=") kc/dec-num)))


(defn parse
  [lines]
  (let [pts (take-while seq lines)
        folds (drop (inc (count pts)) lines)]
    {:points (map (partial kc/value coord-parser) pts)
     :folds (map (partial kc/value fold-parser) folds)}))


(defn fold-step
  [points [[coord fold] n]]
  (let [{tgt true src false} (group-by (fn [pt] (< (coord pt) n)) points)]
    (->> src
         (map (partial fold n))
         (into (set tgt)))))


(defn first-fold
  [{:keys [points] [fst] :folds}]
  (count (fold-step points fst)))


(defn fold-paper
  [{:keys [points folds]}]
  (reduce
   fold-step
   points
   folds))

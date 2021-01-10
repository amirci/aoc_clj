(ns aoc.2018.day10
  (:require
   [blancas.kern.core :as kc :refer [value <*> <$>]]
   [blancas.kern.lexer.java-style :as lx]))


(def coord-expr
  (lx/angles (lx/comma-sep1 lx/dec-lit)))


(def value-expr
  (<$> (fn [[tkn _ coords]] [(keyword tkn) coords])
       (<*> lx/identifier (lx/sym \=) coord-expr)))


(def point-spec-expr
  (<$> (partial into {})
       (<*> value-expr value-expr)))


(defn parse-points [inputs]
  (map (partial value point-spec-expr) inputs))


(defn apply-velocity [{:keys [velocity] :as point}]
  (update point :position #(map + % velocity)))


(defn dimensions [pts]
  (let [pts (set (map :position pts))
        ys (map last pts)
        xs (map first pts)
        miny (apply min ys)
        maxy (apply max ys)
        minx (apply min xs)
        maxx (apply max xs)]
    [minx maxx miny maxy]))


(defn word-height? [{:keys [pts]}]
  (let [[_ _ miny maxy] (dimensions pts)]
    (= 9 (- maxy miny))))


(defn move-step [state]
  (-> state
      (update :rounds inc)
      (update :pts (partial map apply-velocity))))


(defn move-points [input]
  (->> input
       parse-points
       (assoc {:rounds 0} :pts)
       (iterate move-step)
       (drop-while (complement word-height?))
       first))

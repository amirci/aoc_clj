(ns aoc.2022.day9
  (:require [blancas.kern.core :as kc]
            [blancas.kern.lexer.basic :as lex]))

(def add-pos (partial map +))

(defn mul-pos [scalar pos]
  (map
   #(->> %
         (* scalar)
         float
         ((fn [n] (if (pos? n) (Math/ceil n) (Math/floor n))))
         int)
   pos))


(defn square [x] (* x x))

(defn mk-vector [[a b] [c d]]
  [(- c a) (- d b)])

(defn distance [p1 p2]
  (->> (mk-vector p1 p2)
       (map square)
       (apply +)
       (Math/sqrt)
       int))

(defn move-towards [t-pos h-pos]
  (let [v (mk-vector t-pos h-pos)]
    (add-pos t-pos (mul-pos 0.5 v))))

(defn- update-history [state]
  (update state :history conj (last (:tail-knots state))))

(defn- match-knot-in-front [{:keys [h-pos tail-knots] :as state} knot-idx]
  (let [follow (tail-knots knot-idx)
        front (if (zero? knot-idx) h-pos (tail-knots (dec knot-idx)))
        d (distance front follow)]
    (assert (< d 3))
    (cond-> state
      (= 2 d) (update-in [:tail-knots knot-idx] move-towards front))))

(defn- match-tail [state]
  (->> (range 0 (count (:tail-knots state)))
       (reduce match-knot-in-front state)))

(defn- move-t-once [state dir]
  (-> state
      (update :h-pos add-pos dir)
      match-tail
      update-history))

(defn- move-t [dir amount state]
  (->> (repeat amount dir)
       (reduce move-t-once state)))

(def moves
  {\R [0 1]
   \U [-1 0]
   \L [0 -1]
   \D [1 0]})

(def dir-p
  (kc/<$> moves (lex/one-of "RULD")))

(def move-p
  (kc/<$> (fn [[dir amount]] (partial move-t dir amount))
          (kc/<*> dir-p lex/dec-lit)))

(def parse-move (partial kc/value move-p))

(defn move-knots [state f] (f state))

(def multi-tail-state
  {:h-pos [0 0]
   :history #{[0 0]}
   :tail-knots (vec (repeat 9 [0 0]))})

(def single-tail-state
  (assoc multi-tail-state :tail-knots [[0 0]]))

(defn simulate-moves [state moves]
  (->> moves
       (map parse-move)
       (reduce move-knots state)
       update-history ; need one more for the last iteration
       :history
       frequencies
       keys
       count))


(def simulate-single-tail
  (partial simulate-moves single-tail-state))

(def simulate-multi-tail
  (partial simulate-moves multi-tail-state))

(ns aoc.2017.day20
  (:require
   [blancas.kern.core :as kc :refer [value <*> <$>]]
   [blancas.kern.lexer.java-style :as lx]))


(def coord-expr
  (lx/angles (lx/comma-sep1 lx/dec-lit)))


(def space-expr
  (<$> (fn [[c _ coords]] [(keyword (str c)) coords])
       (<*> kc/any-char (lx/sym \=) coord-expr)))


(def particle-expr
  (<$> (partial into {})
       (lx/comma-sep1 space-expr)))


(defn mhd [v]
  (->> v
       (map #(Math/abs %))
       (apply +)))


(defn parse-particles [lines]
  (->> lines
       (map-indexed #(assoc (value particle-expr %2) :idx %1))))



(defn particle-update [{:keys [a v p] :as particle}]
  (let [new-v (map + a v)
        new-p (map + new-v p)]
    (-> particle
        (assoc :v new-v)
        (assoc :p new-p)
        (assoc :dist (mhd new-p)))))


(def tick (partial map particle-update))


(defn magnitude [v]
  (Math/sqrt (apply + (map #(* % %) v))))


(defn closest-particle [input]
  (->> input
       parse-particles
       (map-indexed #(assoc %2 :idx %1))
       (apply min-key (comp magnitude :a))
       :idx))


(defn destroy-collisions [ps]
  (->> ps
       (group-by :p)
       (remove (fn [[k v]] (< 1 (count v))))
       (map (comp first second))))

(defn remove-collisions [input]
  (->> input
       parse-particles
       (iterate (comp tick destroy-collisions))
       (map (juxt count identity))
       (drop 300)
       ffirst))

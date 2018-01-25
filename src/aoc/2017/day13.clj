(ns aoc.2017.day13
  (:require
    [blancas.kern.core :refer :all]
    [blancas.kern.lexer.basic :refer :all]
    [clojure.tools.trace :refer [trace]]))

(def layer-parser
  (bind [depth dec-lit
         _ colon
         rnge dec-lit]
        (return [depth rnge])))

(defn read-layers
  [lines]
  (->> lines
       (map #(value layer-parser %))
       (into {})))

(defn pico->pos
  [pico rng]
  (let [r (- (* rng 2) 2)
        p (mod pico r)]
    (if (>= p rng) (- r p) p)))

(defn severity
  [layers dly idx]
  (let [rng (layers idx)]
    (cond
      (nil? rng) 0
      (zero? (pico->pos (+ idx dly) rng)) (trace (* idx rng))
      :else 0)))

(defn scanners
  [layers]
  (->> layers
       keys
       (apply max)
       inc
       range))

(defn trip-severity
  ([lines] (trip-severity lines 0))
  ([lines dly]
   (let [layers (read-layers lines)
         calc-severity (partial severity layers dly)]
     (->> layers
          scanners
          (map calc-severity)
          (apply +)))))


(defn is-safe?
  [layers dly idx]
  (let [rng (layers idx)]
    (cond
      (nil? rng) true
      (zero? (pico->pos (+ idx dly) rng)) false
      :else true)))

(defn safe-path?
  [layers dly]
  (->> layers
       scanners
       (every? (partial is-safe? layers dly))))

(defn got-caught?
  [layers {:keys [dly] :as st}]
  (-> st
      (update :dly inc)
      (assoc :caught? (not (safe-path? layers dly)))))

(defn safe-trip-delay
  [lines]
  (let [layers (read-layers lines)]
    (->> {:dly 0 :caught? false}
         (iterate (partial got-caught? layers))
         (drop 1)
         (drop-while :caught?)
         first
         :dly
         dec)))



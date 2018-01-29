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
      (nil? rng) nil
      (zero? (pico->pos (+ idx dly) rng)) [idx rng]
      :else nil)))

(defn scanners
  [layers]
  (->> layers
       keys
       (apply max)
       inc
       range))

(defn find-severity
  [dly layers]
  (let [sev (partial severity layers dly)]
    (->> layers
         scanners
         (map sev)
         (remove nil?))))

(defn trip-severity
  ([lines] (trip-severity lines 0))
  ([lines dly]
   (->> lines
        read-layers
        (find-severity dly)
        (map (partial apply *))
        (apply +))))

(defn got-caught?
  [layers {:keys [dly] :as st}]
  (-> st
      (update :dly inc)
      (assoc :caught? (not (empty? (find-severity dly layers))))))

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

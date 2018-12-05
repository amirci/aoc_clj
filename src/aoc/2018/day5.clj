(ns aoc.2018.day5
  (:require 
    [clojure.string :as str]))

(defn reactive?
  [component]
  (->> component
       sort
       reverse
       (map int)
       (apply -)
       (= 32)))

(defn index-reactive? 
  [polymer i]
  (when (< i (dec (count polymer)))
    (->> [i (inc i)]
         (map #(nth polymer %))
         reactive?)))

(defn adjust-reactive
  [^java.util.ArrayList polymer i total]
  (if (index-reactive? polymer i)
    (do
      (.remove polymer (int i))
      (.remove polymer (int i))
      [polymer (inc total)])
    [polymer total]))

(defn reduce-component
  [[polymer]]
  (reduce
    (fn [[p total] i]
      (if (> i (dec (count p)))
        (reduced [p total])
        (adjust-reactive p i total)))
    [polymer 0]
    (range 1 (count polymer))))

(defn reduce-polymer
  [polymer]
  (->> [(java.util.ArrayList. (into [] polymer)) 1]
       (iterate reduce-component)
       (drop-while #(> (second %) 0))
       first
       first))

(defn part-a
  [polymer]
  (count (reduce-polymer polymer)))

(defn remove-unit
  [polymer i]
  (let [l (char i)
        u (java.lang.Character/toUpperCase l)
        pattern (re-pattern (str "[" l u "]"))]
    (when (re-find pattern polymer)
      (str/replace polymer pattern ""))))

(defn part-b
  [polymer]
  (->> (range (int \a) (inc (int \z)))
       (map (partial remove-unit polymer))
       (remove nil?)
       (map part-a)
       (apply min)))

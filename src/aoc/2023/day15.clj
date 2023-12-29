(ns aoc.2023.day15
  (:require [clojure.string :as s]
             [flatland.ordered.map :as om]))


(defn hash-string
  "
  Increase by the ASCII code
  Set value to itself multiplied by 17.
  Set value to the remainder of dividing itself by 256
  "
  [s]
  (reduce (fn [hsh c]
            (-> hsh
                (+ (int c))
                (* 17)
                (mod 256)))
          0
          s))



(defn sum-hash-values [inst]
  (->> (s/split inst #",")
       (map hash-string)
       (apply +)))


(defn- update-lenses [lenses inst]
  (inst lenses))


(defn- add-lens-inst [hsh lens val]
  (fn [lenses]
    (update lenses
            hsh
            (fnil assoc (om/ordered-map)) lens val)))



(defn- remove-lens-inst [hsh lens]
  (fn [lenses]
    (update lenses
            hsh
            (fnil dissoc (om/ordered-map)) lens)))


(defn parse-instruction [inst]
  (->> inst
       (re-matches #"(\w+)([=\-])(\d+)?")
       (#(let [[_ lens op val] %
               hsh (hash-string lens)]
           (if (= "=" op)
             (add-lens-inst hsh lens (Integer/parseInt val))
             (remove-lens-inst hsh lens))))))


(defn calc-power
  "
  One plus the box number of the lens in question.
  The slot number of the lens within the box: 1 for the first lens, 2 for the second lens, and so on.
  The focal length of the lens.
  "
  [[hsh lenses]]
  (->> lenses
       (map-indexed (fn [i [_ focal]]
                      (* (inc hsh) (inc i) focal)))
       (apply +)))


(defn focusing-power [inst]
  (->> (s/split inst #",")
       (map parse-instruction)
       (reduce update-lenses {})
       (map calc-power)
       (apply +)))

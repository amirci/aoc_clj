(ns aoc.2025.day05)

(defn in-range? [ranges id]
  (some (fn [[rl rh]] (<= rl id rh)) ranges))


(defn total-fresh [[ranges ids]]
  (->> ids
       (filter (partial in-range? ranges))
       count))


(defn- merge-overlapping
  "For each range check with the next one to see if they overlap.
  For he last range [pl ph] and the current range [rl rh]
  The cases are:
  When ph < rl --> do not overlap --> just keep the range as it is: ... [pl ph] [rl rh] 
  When rl <= ph and rh > ph  --> ranges can be merged: ... [pl rh]
  When rl <= ph and rh <= ph --> the new range can be dropped: ... [pl ph]
  "
  [[fst & ranges]]
  (reduce
   ;; The invariant says that all the ranges in `adjusted` they do not overlap
   ;; and they include all the numbers covered so far
   (fn [adjusted [rl rh]]
     (let [[pl ph] (peek adjusted)]
       (if (< ph rl)
         (conj adjusted [rl rh])
         (conj (pop adjusted) [pl (max ph rh)]))))
   [fst]
   ranges))


(defn total-range-fresh [[ranges]]
  (->> ranges
       (merge-overlapping)
       (map (fn [[rl rh]] (inc (- rh rl))))
       (apply +')))


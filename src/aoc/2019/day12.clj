(ns aoc.2019.day12)


(defn mk-planet
  [pos vel]
  {:pos pos :vel vel})

(defn time-step
  [planets]
  planets)

(defn moon-time-steps
  [planets]
  (->> planets
       (map #(mk-planet % [0 0 0]))
       (iterate time-step)))

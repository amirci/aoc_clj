(ns aoc.2019.day12)


(defn mk-planet
  [pos vel]
  {:pos pos :vel vel})

(defn apply-gravity
  [planets])

(defn apply-velocity
  [planets]
  (for [planet planets :let [rst (disj planets planet)]]
    (reduce (fn [planet other]
              )
            planet
            rst)))

(defn time-step
  [planets]
  (apply-velocity
   (apply-gravity planets)))

(defn moon-time-steps
  [planets]
  (->> planets
       (map #(mk-planet % [0 0 0]))
       set
       (iterate time-step)))

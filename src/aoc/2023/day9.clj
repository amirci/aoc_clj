(ns aoc.2023.day9)

(defn create-diffs [history]
  (->> history
       (partition 2 1)
       (map (fn [[a b]] (- b a)))))

(defn- build-extrapolation [rows]
  (->> rows
       reverse
       (map last)
       (apply +)))

(defn- build-extrapolation-prev [rows]
  (->> rows
       reverse
       (map first)
       (reduce (fn [xp n] (- n xp)) 0)
       ))

(defn extrapolate-until-zero [history]
  (->> history
       (iterate create-diffs)
       (take-while (partial not-every? zero?))))

(defn extrapolate-next [history]
  (->> history
       extrapolate-until-zero
       build-extrapolation))

(defn extrapolate-prev [history]
  (->> history
       extrapolate-until-zero
       build-extrapolation-prev))

(defn sum-extrapolation-end [histories]
  (->> histories
       (map extrapolate-next)
       (apply +)))

(defn sum-extrapolation-beg [histories]
  (->> histories
       (map extrapolate-prev)
       (apply +)))

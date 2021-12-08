(ns aoc.2021.day7)


(defn mean
  [xs]
  (let [sxs (sort xs)
        half (quot (count sxs) 2)]
    (nth sxs half)))


(defn calc-fuel
  [crabs]
  (let [m (mean crabs)]
    (->> crabs
         (map #(Math/abs (- m %)))
         (apply +))))

(defn dist [n]
  (quot (* n (+ n 1)) 2))

(defn sum-crabs
  [crabs n]
  (apply +
         (for [c crabs]
           (dist (Math/abs (- c n))))))

(defn calc-fuel2
  [crabs]
  (let [m (apply max crabs)]
    (->> m
         inc
         range
         (map (juxt identity (partial sum-crabs crabs) ))
         (apply min-key second)
         second)))

(ns aoc.2023.day6)


; a * b > 9 -> a > 9 / b
; 1 <= a, b <= 6
(defn winners [[duration wnr]]
  (->> (for [a (range 1 duration)]
         (* a (- duration a) ))
       (filter (partial < wnr))))

(defn winners2 [[duration wnr]]
  (let [half (quot duration 2)]
    (->> (range 1 (inc half))
         (filter (fn [n] (> (* n (- duration n)) wnr)))
         first
         (* 2)
         (- duration -1))))

(defn multiply-winning-ways [races]
  (->> races
       (map winners2)
       (apply *)))

(ns aoc.2022.day1)


(defn largest-calorie-sum
  [calories]
  (->> calories
       (map (partial apply +))
       (apply max)))

(defn top3-calories-sum
  [calories]
  (->> calories
       (map (partial apply +))
       (sort-by identity >)
       (take 3)
       (apply +)))



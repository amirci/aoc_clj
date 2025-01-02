(ns aoc.2024.day13)



(defn cheapest-claw
  "a * ax + b * bx = prize-x
   a * ay + b * by = prize-y

    a = (prize-x - b * bx) / ax

  ((prize-x - b * bx) / ax) * ay + b * by = prize-y

  (prize-x / ax - b * bx / ax) * ay + b * by = prize-y

  prize-x * ay / ax - b * bx * ay / ax + b * by = prize-y

  - b * bx * ay / ax + b * by = prize-y - prize-x * ay / ax

  b * (by - bx * ay / ax) = prize-y - prize-x * ay / ax

  b = (prize-y - prize-x * ay / ax) / (by - bx * ay / ax

  "
  [[[ax ay] [bx by] [prize-x prize-y]]]
  (let [b (/ (- prize-y (* prize-x (/ ay ax)))
             (- by (* bx (/ ay ax))))
        a (/ (- prize-y (* b by)) ay)]
    (when (every? integer? [a b])
      (+ (* a 3) b))))


(defn- parse-2-nbrs [prize-line]
  (->> prize-line
       (re-seq #"(\d+)")
       (map second)
       (mapv #(Integer/parseInt %))))


(def parse-prize (partial mapv parse-2-nbrs))


(defn- adjust-prize-pos [adjust prize]
  (-> prize
      (update-in [2 0] + adjust)
      (update-in [2 1] + adjust)))


(defn cheapest-claws
  ([input] (cheapest-claws 0 input))
  ([adjust input]
   (->> input
        (map parse-prize)
        (map (partial adjust-prize-pos adjust))
        (keep cheapest-claw)
        (apply +))))

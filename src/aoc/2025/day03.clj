(ns aoc.2025.day03)

(defn- largest-joltage [[idx j]] (- (* 100 j) idx))

(defn choose-max [joltages]
  (let [indexed (->> joltages (map-indexed vector) vec)
        [idx-1 max-1] (apply max-key
                             largest-joltage
                             (subvec indexed 0 (dec (count indexed))))
        [_ max-2] (apply max-key second (subvec indexed (inc idx-1)))]
    (+ (* 10 max-1) max-2)))


(defn total-joltage [battery]
  (->> battery
       (map choose-max)
       (apply +)))


(defn- max-joltage-between [indexed [last-idx] margin]
  (apply max-key
         largest-joltage
         (subvec indexed (inc last-idx) margin)))


(defn- max-total-joltage [joltages]
  (let [indexed (->> joltages (map-indexed vector) vec)
        len (count indexed)]
    (->> (range 11 -1 -1)
         (reduce
          (fn [acc margin]
            (conj acc
                  (max-joltage-between indexed (last acc) (- len margin))))
          [[-1]])
         (drop 1)
         (map second)
         (reduce (fn [acc d] (+ (* acc 10) d))))))


(defn total-joltage-12 [battery]
  (->> battery
       (map max-total-joltage)
       (apply +')))

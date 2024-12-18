(ns aoc.2024.day11)


(defn count-digits [n]
  (inc (int (Math/log10 n))))

(defn even-count-digits? [n]
  ((every-pred pos? even?) (count-digits n)))

(defn ->nbr [dts]
  (reduce (fn [acc n] (+ (* acc 10) n)) dts))

(defn- split-nbr [stone]
  (let [size (count-digits stone)
        p (apply * (repeat (quot size 2) 10))]
    (->> [(quot stone p) (mod stone p)]
         (map int))))


(defn- tickity [stones]
  (mapcat (fn [stone]
         (cond
           (zero? stone) [1]
           (even-count-digits? stone) (split-nbr stone)
           :else [(* 2024 stone)]))
       stones))


(defn stones-tick [times stones]
  (->> stones
       (iterate tickity)
       (drop times)
       first))


(def count-stones-tick (comp count stones-tick))

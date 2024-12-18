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


(defn tickity [stones]
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

(def safe-plus (fnil + 0))

(defn- update-split [freq stone total]
  (let [[a b] (split-nbr stone)]
    (-> freq
        (update a safe-plus total)
        (update b safe-plus total))))


(defn tickety-freq [freq]
  (->> freq
       (reduce
        (fn [freq [stone total]]
          (cond
            (zero? stone) (update freq 1 safe-plus total)
            (even-count-digits? stone) (update-split freq stone total)
            :else (update freq (* 2024 stone) safe-plus total)))
        {})))


(defn count-stones-tick-o [times stones]
  (->> stones
       frequencies
       (iterate tickety-freq)
       (drop times)
       first
       vals
       (apply +)))


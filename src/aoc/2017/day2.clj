(ns aoc.2017.day2)


(defn to-numbers
  [line]
  (-> line
      (clojure.string/split #"\t")
      (as-> ns (map read-string ns))))

(defn dif-max-min
  [ns]
  (- (apply max ns) (apply min ns)))

(defn div-divisible
  [ns]
  (let [sorted (sort ns)]
    (->> sorted
         (map-indexed (fn [i n]
                        (->> sorted
                             (drop (inc i))
                             (filter #(= 0 (rem % n)))
                             first
                             (conj [n]))))
         (filter (comp not nil? second))
         first
         reverse
         (apply /))))

(defn checksum
  [lines]
  (->> lines
       (map to-numbers)
       (map dif-max-min)
       (apply +)))

(defn checksumb
  [lines]
  (->> lines
       (map to-numbers)
       (map div-divisible)
       (apply + 0)))




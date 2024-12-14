(ns aoc.2024.day1)


(defn total-distances [distances]
  (->> distances
       (map sort)
       (apply map vector)
       (map (partial apply (comp abs -)))
       (apply +)))


(defn similarity-score [[lst1 lst2]]
  (let [freq (frequencies lst2)]
    (->> lst1
         (map (fn [n] (* n (get freq n 0))))
         (apply +))))

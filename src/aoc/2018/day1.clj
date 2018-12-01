(ns aoc.2018.day1)

(defn part-a
  [changes]
  (->> changes
       clojure.string/split-lines
       (map #(Integer/parseInt %))
       (apply +)))


(defn part-b
  [changes]
  (->> changes
       clojure.string/split-lines
       (map #(Integer/parseInt %))
       cycle
       (reduce (fn [[freqs sum] n]
                 (let [sum* (+ sum n)]
                   (if (freqs sum*)
                     (reduced sum*)
                     [(conj freqs sum*) sum*])))
               [#{} 0])))





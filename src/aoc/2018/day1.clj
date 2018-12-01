(ns aoc.2018.day1)

(defn- ->changes
  [file]
  (->> file
       clojure.string/split-lines
       (map #(Integer/parseInt %))))

(defn part-a
  [file]
  (->> file
       ->changes
       (apply +)))

(defn sum-until-repeated
  [[seen sum] n]
  (let [sum* (+ sum n)]
    (if (seen sum*)
      (reduced sum*)
      [(conj seen sum*) sum*])))

(defn part-b
  [file]
  (->> file
       ->changes
       cycle
       (reduce sum-until-repeated [#{} 0])))





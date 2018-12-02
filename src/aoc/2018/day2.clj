(ns aoc.2018.day2)

(defn has-n?
  [n fqs]
  (->> fqs
       (filter (fn [[k v]] (= n v)))
       seq))

(defn part-a
  [box]
  (let [fqs (map frequencies box)]
    (->> [2 3]
         (map #(count (filter (partial has-n? %) fqs)))
         (apply *))))

(defn differences
  [s1 s2]
  (->> (map vector s1 s2)
       (filter #(apply not= %))
       count))

(defn common-letters
  [s1 s2]
  (->> (map vector s1 s2)
       (filter #(apply = %))
       (map first)
       (apply str)))

(defn find-two-matching
  [box seen s]
  (let [match (->> (disj box s)
                   (filter #(-> % (differences s) (= 1)))
                   first)]
    (if match
      (reduced (common-letters s match))
      (conj seen s))))

(defn part-b
  [box]
  (->> box
       (reduce (partial find-two-matching (set box)) [])))


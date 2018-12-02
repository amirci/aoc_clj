(ns aoc.2018.day2)

(defn has-2s?
  [fqs]
  (->> fqs
       (filter (fn [[k v]] (= 2 v)))
       seq))

(defn has-3s?
  [fqs]
  (->> fqs
       (filter (fn [[k v]] (= 3 v)))
       seq))

(defn checksum
  [box]
  (let [fqs (map frequencies box)
        t2 (count (filter has-2s? fqs))
        t3 (count (filter has-3s? fqs))]
    (* t2 t3)))


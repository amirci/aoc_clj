(ns aoc.2019.day8)

(defn layer-freq
  [msg len]
  (->> msg
       (partition len)
       (map frequencies)))


(defn find-layer-with-min-zero
  [msg len]
  (->> len
       (layer-freq msg)
       (apply min-key #(get % \0))))


(defn min-layer-total-ones-and-twos
  [msg len]
  (->> (find-layer-with-min-zero msg len)
       ((juxt #(get % \1) #(get % \2)))
       (apply *)))

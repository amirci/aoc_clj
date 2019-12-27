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


(defn render-pixel
  [& ps]
  (first (filter #(not= \2 %) ps)))

(def mk-readable {\1 \*  \0 \ })

(defn decode-msg
  [msg width len]
  (->> msg
       (partition len)
       (apply map render-pixel)
       (map mk-readable)
       (partition width)
       (map (partial apply str))))

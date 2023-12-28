(ns aoc.2023.day13)



(defn mirror? [note n]
  (->> (for [i (range 0 n)
             :let [b (+ n i)]
             :when (< b (count note))]
         [(- n i 1) b])
       seq
       (#(and %
              (every? (fn [[a b]] (= (note a) (get note b))) %)))))

(defn find-mirrors
  "Finds the lines which are a mirror horizontally"
  [note]
  (->> (range 1 (inc (count note)))
       (filter (partial mirror? note))))

(defn find-mirror
  "Finds the first line which is a mirror horizontally"
  [note]
  (->> note
       find-mirrors
       first))

(defn find-h-mirrors [note]
  (->> note
       find-mirrors
       (map (partial * 100))))

(defn find-h-mirror [note]
  (->> note
       find-mirror
       (#(cond-> %
           (number? %) (* 100)))))

(defn find-v-mirrors [note]
  (->> note
       (apply map vector)
       vec
       find-mirrors))

(defn find-v-mirror [note]
  (->> note
       find-v-mirrors
       first))

(defn find-mirror-line [note]
  (or (find-v-mirror note)
      (find-h-mirror note)))


(defn sum-mirrors [notes]
  (->> notes
       (map find-mirror-line)
       (apply +)))


(defn update-note-smudge [note pos]
  (update-in note pos {\. \# \# \.}))


(defn find-different-mirror-line [old note]
  (->> note
       ((juxt find-v-mirrors find-h-mirrors))
       (apply concat)
       (filter (every-pred some? (partial not= old)))
       first))

(defn fix-smudge [note]
  (let [old (find-mirror-line note)]
    (->> (for [x (range (count note) )
               y (range (count (first note)) )]
           [x y])
         (map (partial update-note-smudge note))
         (keep (partial find-different-mirror-line old))
         first)))


(defn sum-mirrors-fixing-smudges [notes]
  (->> notes
       (map fix-smudge)
       (apply +)))

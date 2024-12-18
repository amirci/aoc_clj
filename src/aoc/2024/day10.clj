(ns aoc.2024.day10)


(def paths (for [x [-1 0 1] y [-1 0 1]
                 :when (not= (abs x) (abs y) )] [x y]))


(defn gradual? [prev step]
  (and step (= 1 (- step prev))))


(defn valid-steps [topo-map [x y]]
  (let [prev (topo-map [x y])]
    (for [[dx dy] paths
          :let [x1 (+ x dx) y1 (+ y dy)
                step (topo-map [x1 y1])]
          :when (gradual? prev step)]
          [x1 y1])))


(defn find-trails [topo-map results path [x y]]
  (let [path (conj path [x y])]
    (if (= 9 (topo-map [x y]))
      (conj results path)
      (reduce (fn [results potential]
                (find-trails topo-map results path potential))
              results
              (valid-steps topo-map [x y])))))


(defn trailhead-score [topo-map start]
  (->> start
       (find-trails topo-map #{} [])
       (map last)
       set
       count))


(defn sum-trailheads [f topo-map]
  (->> topo-map
       (filter (comp zero? second))
       (map first)
       (map (partial f topo-map))
       (apply +)))


(def sum-scores-trailheads (partial sum-trailheads trailhead-score))


(defn trailhead-rating [topo-map start]
  (->> start
       (find-trails topo-map #{} [])
       count))


(def sum-rating-trailheads (partial sum-trailheads trailhead-rating))

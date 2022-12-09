(ns aoc.2022.day8)

(defn mk-indexes [forest]
  (for [x (range 1 (dec (count forest)))
        y (range 1 (dec (count forest)))]
    [x y]))

(def cardinals
  [[1 0] [-1 0] [0  1] [0 -1]])

(defn- pos-add [p1 p2]
  (map + p1 p2))

(defn edge? [forest [x y]]
  (let [width (dec (count forest))]
    (or (zero? x)
        (zero? y)
        (= width x)
        (= width y))))

(defn shorter-than [height forest pos]
  (->> pos
       (get-in forest)
       (> height)))

(defn forest-utils [forest]
  {:edge? (partial edge? forest)
   :not-edge? (complement (partial edge? forest))
   :tree-height (partial get-in forest)
   :forest? (partial get-in forest)})

(defn visible-dir? [forest pos dir]
  (let [{:keys [tree-height forest? edge? not-edge?]} (forest-utils forest)
        shorter? (partial shorter-than (tree-height pos) forest)]
    (->> pos
         (iterate (partial pos-add dir))
         (drop 1)
         (drop-while (every-pred forest? not-edge? shorter?))
         first
         ((every-pred forest? edge? shorter?)))))


(defn visible? [forest pos]
  (let [tree (get-in forest pos)]
    (and (pos? tree)
         (some (partial visible-dir? forest pos) cardinals))))

(defn edges [forest]
  (let [c (count forest)]
    (+ c (- c 2) (* 2 (dec c)))))

(defn count-visible-trees [forest]
  (->> forest
       mk-indexes
       (filter (partial visible? forest))
       count
       (+ (edges forest))))

(defn count-trees [forest pos dir]
  (let [{:keys [forest? tree-height]} (forest-utils forest)
        forest? (comp forest? first)
        move (partial pos-add dir)
        target-height (tree-height pos)
        second-not-shorter? (fn [[_ dist prev]]
                              (or (<= dist 1)
                                  (< prev target-height)))]
    (->> [pos 0]
         (iterate (fn [[p dist]]
                    [(move p) (inc dist) (tree-height p)]))
         (drop 1)
         (take-while (every-pred forest? second-not-shorter?))
         last
         second)))

(defn- scenic-score [forest pos]
  (->> cardinals
       (map (partial count-trees forest pos))
       (apply *)))

(defn highest-scenic-score [forest]
  (->> forest
       mk-indexes
       (map (partial scenic-score forest))
       (apply max)))


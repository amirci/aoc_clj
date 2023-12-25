(ns aoc.2023.day11)

(defn empty-rows [input]
  (->> input
       (map-indexed vector)
       (map (fn [pair] (update pair 1 set)))
       (filter (fn [[_ s]] (= 1 (count s))))
       (map first)))


(defn empty-cols [input]
  (->> input
       (apply map vector)
       empty-rows))

(defn split-multiple [idxs coll]
  (->> idxs
       (reduce
        (fn [[split pending prev] n]
          (let [idx (- n prev)
                [new-split pending] (split-at idx pending)]
            [(conj split new-split) pending n]))
        [[] coll 0])
       ((fn [[split pending]]
          (cond-> split
            (not-empty pending) (conj pending))))))


(defn insert-multiple [idxs sep coll]
  (->> coll
       (split-multiple idxs)
       (interpose [sep])
       (apply concat)))


(defn double-empty [input]
  (let [[rows cols] ((juxt empty-rows empty-cols) input)
        col-size (+ (count (first input)) (count cols))
        empty-row (repeat col-size \.)]
    (->> input
         (map (partial insert-multiple cols \.))
         (insert-multiple rows empty-row)
         (map vec)
         vec)))


(defn ->galaxy-map [[fst :as input]]
  (->> (for [x (range (count input)) y (range (count fst))
             :let [galaxy (get-in input [x y])]
             :when (= \# galaxy)]
         [x y])
       set))


(defn distance [[a b] [c d]]
  (+ (Math/abs (- a c)) (Math/abs (- b d))))

(defn- sum-paths-between-galaxies [galaxies]
  (->> galaxies
       (reduce
        (fn [[pending acc] gxy]
          (let [pending (disj pending gxy)]
            (->> pending
                 (map (partial distance gxy))
                 (apply + acc)
                 (vector pending))))
        [(set galaxies) 0])
       second))

(defn empty-factor [factor rows cols [x y]]
  (let [x-empty (filter #(< % x) rows)
        y-empty (filter #(< % y) cols)]
    [(+ x (* (dec factor) (count x-empty)))
     (+ y (* (dec factor) (count y-empty)))]))

(defn shortest-paths [factor input]
  (let [f (juxt empty-rows empty-cols)
        [rows cols] (f input)]
    (->> input
         ->galaxy-map
         (map (partial empty-factor factor rows cols))
         sum-paths-between-galaxies)
    ))

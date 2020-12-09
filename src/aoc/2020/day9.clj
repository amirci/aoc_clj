(ns aoc.2020.day9)

(defn ->pair-sum [coll]
  (for [x coll y coll :when (not= x y)]
    (+ x y)))

(defn check-sum? [coll]
  (let [[n & rst] (reverse coll)]
    (->> rst
         ->pair-sum
         (filter (partial = n))
         first)))

(defn xmas-invalid [pby nbrs]
  (->> nbrs
       (partition (inc pby) 1)
       (map-indexed vector)
       (filter (complement (comp check-sum? second)))
       first
       (#(-> %
             (update 0 + pby)
             (update 1 last)))))


(defn subvecs [v start]
  (->> (count v)
       inc
       (range (+ 2 start))
       (map (partial subvec v start))))

(defn candidates [coll]
  (let [v (vec coll)]
    (->> (count coll )
         dec
         (range 0)
         (mapcat (partial subvecs v)))))


(defn weakness [pby nbrs]
  (let [[idx invalid] (xmas-invalid pby nbrs)
        [v] (split-at idx nbrs)]
    (->> nbrs
         (split-at idx)
         first
         candidates
         #_(map (juxt identity (partial apply +)))
         (filter #(= invalid (apply + %)))
         first
         sort
         ((juxt first last))
         (apply +))))



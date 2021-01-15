(ns aoc.2018.day12)

(defn ->plant-set [s]
  (->> s
       vec
       (map-indexed vector)
       (filter (comp (partial = \#) second))
       (map first)
       (apply sorted-set)))


(defn ->pattern [idx plants]
  (->> (range (- idx 2) (+ 3 idx))
       (map #(if (plants %) \# \.))))


(defn plant-rule [rules plants idx]
  (->> plants
       (->pattern idx)
       rules
       (#(when (= \# %) idx))))


(defn ->range [plants]
  (let [mn (apply min plants)
        mx (apply max plants)]
    (range (- mn 5) (+ mx 6))))


(defn generation [rules plants]
  (->> plants
       ->range
       (keep (partial plant-rule rules plants))
       (apply sorted-set)))


(defn generations [rules plants rounds]
  (->> plants
       (iterate (partial generation rules))
       (drop rounds)
       first))


(defn gen-sum-diff [rules {:keys [diff plants sum diff-count] :as state}]
  (let [new-gen (generation rules plants)
        new-sum (apply + new-gen)
        new-diff (- new-sum sum)]
    (-> state
        (assoc :sum new-sum)
        (assoc :diff new-diff)
        (update :diff-count (if (= new-diff diff) inc (constantly 0)))
        (update :base (if (= new-diff diff) identity (constantly sum)))
        (assoc :plants new-gen)
        (update :round inc))))


(defn find-stable [rules plants]
  "Finds when the difference stabilizes to the same number for 20 iterations"
  (->> plants
       (assoc {:round 0 :base 0 :diff-count 0 :sum (apply + plants) :diff 0} :plants)
       (iterate (partial gen-sum-diff rules))
       (drop-while #(< (:diff-count %) 20))
       first
       (#(-> %
             (select-keys [:diff :round :base])
             (update :round - 21)))))


(defn long-gen [rules plants rounds]
  (let [{:keys [diff base] start :round} (find-stable rules plants)]
    (+ base (* diff (- rounds start)))))

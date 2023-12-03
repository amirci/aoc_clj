(ns aoc.2023.day3)


(defn re-seq-pos [pattern string] 
  (let [m (re-matcher pattern string)] 
    ((fn step []
       (when (. m find) 
         (cons {:start (. m start) :end (. m end) :match (. m group)} 
               (lazy-seq (step))))))))

(defn neighbours [row start end]
  (->> [(for [i (range (dec start) (inc end)) j [(dec row) (inc row)]] [j i])
        [[row (dec start)] [row end]]]
       (apply concat)))


(defn not-digit? [c]
  (not (<= 48 (int c) 57)))

(defn part-number? [schema [row {:keys [start end]}]]
  (->> (neighbours row start end)
       (map #(get-in schema %))
       set
       (#(disj % nil \.) )
       (some not-digit?)))

(defn find-part-numbers [schema]
  (->> schema
       (map-indexed (fn [i s] (vector i (re-seq-pos #"\d+" s))))
       (mapcat (fn [[i coll]] (map (partial vector i) coll)))
       (filter (partial part-number? schema))
       (map (fn [pm] (update-in pm [1 :match] #(Integer/parseInt %))))))

(defn sum-part-numbers [schema]
  (->> schema
       find-part-numbers
       (map (comp :match second))
       (apply +)))


(defn find-row-gears [[i row]]
  (->> row
       (map-indexed vector)
       (filter (comp (partial = \*) second))
       (map (fn [[j]] [i j]))))


(defn find-gears [schema]
  (->> schema
       (map-indexed vector)
       (mapcat find-row-gears )))


(defn- close-to-gear? [gear [row {:keys [start end]}]]
  (-> (neighbours row start end)
      set
      (contains? gear)))


(defn- find-close-pns [pns gear]
  (->> pns
       (filter (partial close-to-gear? gear))
       (map (comp :match second))
       (vector gear)))

(defn sum-gear-ratios [schema]
  (let [pns (find-part-numbers schema)]
    (->> schema
         (find-gears)
         (map (partial find-close-pns pns))
         (filter #(= 2 (count (second %))))
         (map #(apply * (second %)))
         (apply +))))



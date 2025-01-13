(ns aoc.2024.day16)


(def east [0 1])


(defn h-or-v? [dx dy]
  (= 1 (->> [dx dy] (map abs) (apply +))))

(defn neighbours [wall? cost [pos dir]]
  (->> (for [dx [-1 0 1] dy [-1 0 1]
             :let [[x y] (mapv + pos [dx dy])
                   new-cost (if (= dir [dx dy]) 1 1001)]
             :when (and (h-or-v? dx dy)
                        (not (wall? [x y])))]
         [(+ new-cost cost) [[x y] [dx dy]]])))


(defn find-all-paths [target-cost {:keys [start end walls]} costs]
  (loop [found #{}
         [[pt dir cost hst :as fst] & rst] [[start east 0 #{}]]]
    (cond
      (nil? fst) (inc (count found) )
      (= pt end) (recur (into found hst) rst)
      :else (let [pending (->> (neighbours walls cost [pt dir])
                               (keep (fn [[new-cost [new-pt new-dir :as new]]]
                                       (cond
                                         (< (costs new Integer/MAX_VALUE) new-cost) nil
                                         (< target-cost new-cost) nil
                                         :else [new-pt new-dir new-cost (conj hst pt)]))))]
              (recur found (concat pending rst))))))


(defn cheapest-path [{:keys [start end walls] :as input}]
  (loop [pending (sorted-map 0 #{[start east]})
         visited? {}]
    (let [[old-cost pts] (first pending)]
      (if (some (comp (partial = end) first) pts)
        [old-cost (find-all-paths old-cost input visited?)]
        (let [pending (->> pts
                           (mapcat (partial neighbours walls old-cost))
                           (remove (comp visited? second))
                           (reduce (fn [new-pending [cost pt]]
                                     (update new-pending cost (fnil conj #{}) pt))
                                   (dissoc pending old-cost)))]
          (recur pending
                 (->> pts
                      (map #(vector % old-cost))
                      (into visited?))))))))


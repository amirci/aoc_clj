(ns aoc.2017.day25)


(def ->move-op {:right inc :left dec})

(def ->val-op [disj conj])


(defn turing [spec {:keys [values current pos] :as state}]
  (let [[zero one] (spec current)
        [val move nxt] (if (values pos) one zero)]
    (-> state
        (update :values (->val-op val) pos)
        (update :pos (->move-op move))
        (assoc :current nxt))))


(defn checksum [input init-state rounds]
  (->> {:current init-state :pos 0 :values #{}}
       (iterate (partial turing input))
       (drop rounds)
       first
       :values
       count))

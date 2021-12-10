(ns aoc.2021.day10)


(def match (->> "[]()<>{}"
                 (partition 2)
                 (map vec)
                 (into {})))

(def open? (set (keys match)))


(def score
  {\) 3 \] 57 \} 1197 \> 25137})


(defn parse
  [line]
  (loop [open () [fst & rst] line]
    (cond
      (not fst) (if (seq open)
                  [:incomplete open]
                  :ok)
      (open? fst) (recur (conj open fst) rst)
      (= (match (peek open)) fst) (recur (pop open) rst)
      :else [:corrupt fst])))


(defn result-for?
  [k result]
  (and (coll? result)
       (= k (first result))))


(def corrupt-char second)


(defn sum-illegal
  [lines]
  (->> lines
       (map parse)
       (filter (partial result-for? :corrupt))
       (map corrupt-char)
       (map score)
       (apply +)))


(def auto-score {\) 1 \] 2 \} 3 \> 4})


(defn score-autocomplete
  [line]
  (reduce
   (fn [acc c] (+ (* acc 5) (auto-score c)))
   0
   line))


(defn autocomplete
  [line]
  (->> line
       (map match)))


(def pending-part second)


(defn autocomplete-winner
  [lines]
  (->> lines
       (map parse)
       (filter (partial result-for? :incomplete))
       (map pending-part)
       (map autocomplete)
       (map score-autocomplete)
       sort
       (#(nth % (quot (count %) 2)))))

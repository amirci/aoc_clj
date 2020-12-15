(ns aoc.2020.day15)

(defn init [input]
  {:turn 1 :spoken {} :input input})

(def spoken-twice (comp (partial < 1) count))

(defn diff [[a b]] (- a b))

(defn keep-last [[a b] nbr]
  (cond-> [nbr]
    a (conj a)))

(defn speak-next
  [{:keys [last spoken turn input] [fst] :input :as state}]
  (let [sl (spoken last)
        nbr (cond
              (<= turn (count input)) (get input (dec turn))
              (spoken-twice sl) (diff sl)
              :else 0)]
    (-> state
        (update :turn inc)
        (assoc :last nbr)
        (update :spoken update nbr keep-last turn))))

(defn play [input turn]
  (->> input
       init
       (iterate speak-next)
       (drop turn)
       first
       :last))


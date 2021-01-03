(ns aoc.2017.day17)


(defn follow [idx steps nbrs]
  (->> idx
       (iterate nbrs)
       (drop steps)
       first))

(defn links [nbrs]
  (->> 0
       (iterate nbrs)
       (take (count nbrs))))


(defn spin [steps {:keys [idx nbrs] :as state}]
  #_(when (zero? (mod (count nbrs) 100000))
    (println "IDX" idx "-" (count nbrs)))
  (let [idx* (time (follow idx steps nbrs))
        after (nbrs idx*)
        len (count nbrs)]
    (-> state
        (assoc :idx* idx*)
        (update :nbrs conj after)
        (assoc-in [:nbrs idx*] len)
        (assoc :idx len))))


(defn find-after [{:keys [idx nbrs]}]
  (nbrs idx))


(defn spinlock [steps rounds]
  (->> {:idx 0 :nbrs [0]}
       (iterate (partial spin steps))
       (drop rounds)
       first
       find-after))


(defn angry-step [steps {:keys [idx round] :as state}]
  (let [idx* (inc (mod (+ steps idx) round))]
    (-> state
        (assoc :idx idx*)
        (update :after #(if (= 1 idx*) round %))
        (update :round inc))))

(defn spinlock-angry [steps rounds]
  (->> {:idx 0 :after 0 :round 1}
       (iterate (partial angry-step steps))
       (drop rounds)
       first
       :after))


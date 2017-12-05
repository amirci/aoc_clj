(ns aoc.2017.day5)


(defn maze-step
  [{:keys [ptr maze] :as state}]
  (-> state
      (update-in [:maze ptr] inc)
      (update :ptr  #(+ % (get maze ptr)))))

(defn maze-step-b
  [{:keys [ptr maze] :as state}]
  (let [offset (get maze ptr)]
    (-> state
        (update-in [:maze ptr] (if (> offset 2) dec inc))
        (update :ptr #(+ % offset)))))


(defn count-steps-to-exist-maze
  ([maze] (count-steps-to-exist-maze maze maze-step))
  ([maze f-step]
   (->> maze
        (map read-string)
        (map-indexed vector)
        (into {})
        (hash-map :ptr 0 :maze)
        (iterate f-step)
        (take-while #(< (:ptr %) (count maze)))
        count)))

(defn count-steps-to-exist-maze-b
  [maze]
  (count-steps-to-exist-maze maze maze-step-b))


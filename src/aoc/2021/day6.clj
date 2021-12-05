(ns aoc.2021.day6)

(defn new-tick
  [fish]
  (let [{zs true nz false} (group-by zero? fish)
        cz (count zs)]
    (->> nz
         (map dec)
         (concat (repeat cz 6)
                 (repeat cz 8)))))

(def simulation-seq (partial iterate new-tick))

(defn simulate
  [fish n]
  (->> fish
       simulation-seq
       (drop n)
       first
       count))

(defn init
  [fish]
  (reduce
   (fn [v [idx n]] (assoc v idx n))
   [0 0 0 0 0 0 0 0 0]
   (frequencies fish)))


(defn shift-vec
  [v]
  (->> v
       cycle
       (drop 1)
       (take 9)
       vec))

(defn tick
  [[z :as fish]]
  (-> fish
      shift-vec
      (update 6 + z)))

(defn simulate2
  [fish n]
  (->> fish
       init
       (iterate tick)
       (drop n)
       first
       (apply +)))


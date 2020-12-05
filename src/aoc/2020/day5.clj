(ns aoc.2020.day5)


(def lower? (partial = :lower))

(defn bstep [[l u] k]
  (let [half (+ l (quot (- u l) 2))]
    (if (lower? k)
      [l half]
      [(inc half) u])))

(def ->rowk {\F :lower \B :upper})

(def ->colk {\L :lower \R :upper})

(defn bsearch [rng coll]
  (reduce
   (fn [r step]
     (let [[a b] (bstep r step)]
       (if (= a b)
         (reduced a)
         [a b])))
   rng
   coll))

(def rows [0 127])

(def cols [0 7])

(defn calc-seat [pass]
  (->> pass
       (split-at 7)
       (map #(map %1 %2) [->rowk ->colk])
       (map #(bsearch %1 %2) [rows cols])
       vec
       ((fn [[r c]] [r c (+ c (* r 8))]))))


(defn ->seat-ids [passes]
  (->> passes
       (map calc-seat)
       (map last)))


(defn highest-seat-id [passes]
  (->> passes
       ->seat-ids
       (apply max)))

(def all-ids (set (range 80 927)))

(defn missing-id [passes]
  (->> passes
       ->seat-ids
       set
       (clojure.set/difference all-ids)
       first))


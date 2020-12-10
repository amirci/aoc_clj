(ns aoc.2020.day10)


(defn jolt-diff [adapters]
  (->> 0
       (conj adapters)
       sort
       (partition 2 1)
       (map (comp (partial apply -) reverse))
       frequencies
       (#(update % 3 inc))))

(defn mult-jolt-diff [adapters]
  (->> adapters
       jolt-diff
       (#(select-keys % [1 3]))
       vals
       (apply *)))


(defn find-fitting [idx adapters]
  (if (= (dec (count adapters)) idx)
    []
    (let [fst (get adapters idx)]
      (->> (+ idx 4)
           (min (count adapters))
           (range (inc idx))
           (filter #(< (- (get adapters %) fst) 4))))))

(def ffit (memoize find-fitting))

(declare arrgm)

(defn arrg* [idx adapters]
  (let [branches (ffit idx adapters)]
    (if (seq branches)
      (apply + (map #(arrgm % adapters) branches))
      1)))

(def arrgm (memoize arrg*))

(defn arrangements [adapters]
  (->> 0
       (conj adapters)
       sort
       vec
       (arrgm 0)))

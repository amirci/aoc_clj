(ns aoc.2025.day07)

(defn- valid-row? [length [row]]
  (< -1 row length))


(defn- split-beam-if-valid [length width visited [x y :as pos]]
  (when (and (not (visited pos)) (valid-row? length pos))
    (->> [-1 +1]
         (map (partial + y))
         (filter #(< -1 % width))
         (map (partial vector x))
         (cons (conj visited pos)))))


(defn- follow-beam-until-splitter [length splitters start]
  (->> start
       (iterate #(update % 0 inc))
       (drop 1)
       (drop-while #(and (valid-row? length %)
                         (not (splitters %))))
       first))


(defn count-beam-split*
  [{:keys [start length width splitters visited] :as manifold}]
  (->> start
       (follow-beam-until-splitter length splitters)
       (split-beam-if-valid length width visited)
       (#(if-let [[new-visited & beams] %]
           (reduce
            (fn [acc new-start]
              (count-beam-split* (assoc manifold :start new-start :visited acc)))
            new-visited
            beams)
           visited))))


(defn count-beam-split [manifold]
  (->> (assoc manifold :visited #{})
       count-beam-split*
       count))


(def count-beam-paths*
  (memoize
   (fn [{:keys [length splitters] :as manifold} [x y]]
   (cond
     (= x length) 1
     (splitters [x y]) (+ (count-beam-paths* manifold [x (dec y)])
                          (count-beam-paths* manifold [x (inc y)]))
     :else (count-beam-paths* manifold [(inc x) y])))))


(defn count-beam-paths [manifold]
  (-> manifold
      (count-beam-paths* (:start manifold))))

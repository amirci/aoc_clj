(ns aoc.2017.day7)


(defn is-not-dependency?
  [assignments w]
  (->> assignments
       (filter #(-> (drop 2 %) set (as-> s (s w))))
       first
       nil?))

(defn split-terms
  [lines]
  (->> lines 
       (map #(clojure.string/replace % ", " " "))
       (map #(clojure.string/split % #" "))))

(defn mk-assignments
  [lines]
  (->> lines 
       split-terms
       (filter #(> (count %) 2))))

(defn find-bottom
  [lines]
  (let [assignments (mk-assignments lines)]
    (->> assignments
         (map first)
         (filter #(is-not-dependency? assignments %))
         first)))

(defn mk-discs
  []
  )
(defn find-unbalance
  [lines]
  (let [discs (mk-discs lines)]
    discs))

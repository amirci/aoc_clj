(ns aoc.2019.day6)

(defn parse-orbit
  [orbit]
  (clojure.string/split orbit #"\)"))

(defn update-orbit-tree
  [m [parent child]]
  (update m parent #(conj (or % []) child)))

(defn mk-orbit-tree
  [spec]
  (->> spec
       (map parse-orbit)
       (reduce update-orbit-tree {})))

(defn get-children
  [node tree]
  (get tree node []))

(defn has-children?
  [node tree]
  (seq (get-children node tree)))

(defn mk-children
  [node tree h]
  (when node
    (map vector
         (get-children node tree)
         (repeat (inc h)))))

(defn count-total-deps
  [root tree]
  (loop [[[node h] & rst] (mk-children root tree 0) total 0]
    (let [children (mk-children node tree h)]
      (cond
        (nil? node) total
        (seq children) (recur (concat children rst) (+ h total))
        :else (recur rst (+ total h))))))

(defn total-orbits
  [orbit-spec]
  (->> orbit-spec
       mk-orbit-tree
       (count-total-deps "COM")))

(defn find-parent
  [tree node]
  (->> tree
       (filter (fn [[k coll]] (some (partial = node) coll)))
       ffirst))

(defn parents-of
  [node tree]
  (->> node
       (iterate (partial find-parent tree))
       (take-while seq)
       reverse))

(defn min-orbit-transfer
  [spec]
  (let [tree (mk-orbit-tree spec)]
    (loop [[y1 & yrst :as you] (parents-of "YOU" tree)
           [s1 & srst :as san] (parents-of "SAN" tree)]
      (if (= y1 s1)
        (recur yrst srst)
        (->> [you san]
             (map count)
             (apply + -2))))))

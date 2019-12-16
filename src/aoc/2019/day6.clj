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

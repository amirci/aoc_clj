(ns aoc.2017.day24
  (:require [clojure.set :as cs]))


(defn matching-adapter [node tgt]
  (->> node
       last
       set
       (cs/intersection (set tgt))
       first))


(defn matching [{:keys [pending node]}]
  (->> pending
       (map (juxt vec (partial matching-adapter node)))
       (filter second)
       (map (fn [[[x y :as tgt] v]]
              (conj tgt (if (= x v) [y] [x]))))))

(def matching* (memoize matching))


(defn branch? [node]
  (seq (matching* node)))


(defn mk-branch [branch [x y :as adapter]]
  (-> branch
      (update :path conj [x y])
      (update :pending disj [x y])
      (update :total + x y)
      (assoc :node adapter)))


(defn branches [node]
  (->> node
       matching*
       (map (partial mk-branch node))))


(defn find-bridges [adapters]
  (->> adapters
       set
       (assoc {:node [0 [0]]} :path [] :total 0 :pending)
       (tree-seq branch? branches)
       (remove branch?)))

(defn strongest-bridge [adapters]
  (->> adapters
       find-bridges
       (map :total)
       (apply max)))

(defn longest-bridge [adapters]
  (->> adapters
       find-bridges
       (group-by (comp count :path))
       (apply max-key first)
       second
       (apply max-key :total)
       :total))

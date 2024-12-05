(ns aoc.2024.day5
  (:require [clojure.set :as cjs]))

(defn contains-order? [ordering [fst & rst]]
  (let [contains? (ordering fst)]
    (and contains? (every? contains? rst))))

(defn right-order? [ordering pages]
  (->> pages
       (iterate rest)
       (take-while #(< 1 (count %)))
       (every? (partial contains-order? ordering))))


(defn multiset [pairs]
  (reduce
   (fn [m [a b]]
     (update m a (fnil conj #{}) b))
   {}
   pairs))


(defn- middle [v]
  (get v (quot (count v) 2)))


(defn correct-order-middle-sum [[ordering changes]]
  (let [ordering (multiset ordering)]
    (->> changes
         (filter (partial right-order? ordering))
         (map middle)
         (apply +))))


(defn reorder [ordering pages]
  (let [pages (set pages)
        k-fn #(count
               (cjs/intersection (ordering %)
                                 (disj pages %)))]
    (->> pages
         (sort-by k-fn)
         reverse
         vec)))


(defn incorrect-order-middle-sum [[ordering changes]]
  (let [ordering (multiset ordering)]
    (->> changes
         (remove (partial right-order? ordering))
         (map (partial reorder ordering))
         (map middle)
         (apply +))))

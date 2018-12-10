(ns aoc.2018.day8
  (:require 
    [clojure.tools.trace :refer [trace]]
    [clojure.string :as str]))

(defn mk-node
  [input]
  (let [[cc mc] (take 2 input)
        input* (drop 2 input)
        node {:children [] :metadata [] :chld-count cc :mtd-count mc}]
    [node input*]))

(declare read-tree)

(defn read-children
  [ct input]
  (reduce
    (fn [[children input] _]
      (let [[child input] (read-tree input)]
        [(conj children child) input]))
    [[] input]
    (range ct)))

(defn read-tree
  [input]
  (let [[{:keys [chld-count mtd-count] :as node} input] (mk-node input)
        [children input] (if (zero? chld-count) 
                           [[] input] 
                           (read-children chld-count input))
        metadata (take mtd-count input)
        input (drop mtd-count input)]
    [(assoc node :children children :metadata metadata)
     input]))

(defn calc-with-index
  [{:keys [chld-count children metadata]}]
  (if (empty? children)
    (apply + metadata)
    (let [children (->> metadata
                        (filter #(<= % chld-count))
                        (map #(nth children (dec %))))]
      (->> children
           (map calc-with-index)
           (apply +)))))

(defn part-a
  [input]
  (->> input
       read-tree
       first
       (tree-seq (comp seq :children) :children)
       (mapcat :metadata)
       (apply +)))

(defn part-b
  [input]
  (calc-with-index (first (read-tree input))))

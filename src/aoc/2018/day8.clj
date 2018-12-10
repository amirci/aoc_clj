(ns aoc.2018.day8
  (:require 
    [clojure.tools.trace :refer [trace]]
    [clojure.string :as str]))

(defn update-pending-children
  [pending]
  (if (seq pending)
    (let [[chld mtd] (last pending)
          pending* (pop pending)]
      (conj pending* [(dec chld) mtd]))
    pending))

(defn add-pending-parent
  [pending [chld mtd :as node]]
  (if (zero? chld)
    pending
    (conj pending node)))

(defn add-metadata
  [mtd total input]
  [(reduce + total (take mtd input))
   (drop mtd input)])

(defn check-zero-children
  [chld mtd total input]
  (if (zero? chld)
    (add-metadata mtd total input)
    [total input]))


(defn sum-finished-parents
  [t p i]
  (loop [total t pending p input i]
    (let [[chld mtd] (last pending)
          [total* input*] (and mtd (add-metadata mtd total input))]
      (if (and chld (zero? chld))
        (recur total* (pop pending) input*)
        [total pending input]))))

(defn read-node
  [[total pending input]]
  (let [[chld mtd :as node] (vec (take 2 input))
        input* (drop 2 input)
        [total* input*] (check-zero-children chld mtd total input*)
        pending* (update-pending-children pending)
        pending* (add-pending-parent pending* node)]
    (sum-finished-parents total* pending* input*)))

(defn part-a
  [input]
  (->> [0 [] input]
       (iterate read-node)
       (drop-while (comp seq last))
       ffirst))


;;; Part B

(defn check-leaf-node
  [{:keys [chld-count mtd-count] :as node} input]
  (if (zero? chld-count)
    [(assoc node :metadata (take mtd-count input))
     (drop mtd-count input)]
    [node input]))

(defn mk-node
  [input]
  (let [[cc mc] (take 2 input)
        input* (drop 2 input)
        node {:children [] :metadata [] :chld-count cc :mtd-count mc}]
    (check-leaf-node node input*)))

(defn update-top
  [pending f]
  (if (seq pending)
    (let [node (last pending)]
      (conj (pop pending) (f node)))
    pending))

(defn add-pending-parent
  [pending {:keys [chld-count] :as node}]
  (if (zero? chld-count)
    pending
    (conj pending node)))

(defn reduce-pending-nodes
  [p i]
  (loop [pending (pop p)
         {:keys [chld-count mtd-count] :as top} (last p) 
         input i]
    (if (empty? pending)
      [[top] input]
      (if (and chld-count (zero? chld-count))
        (recur (pop pending)
               (-> top
                   (update :children conj top)
                   (assoc :metadata (take mtd-count input)))
               (drop mtd-count input))
        [pending input]))))

(defn add-pending-or-to-parent
  [pending {:keys [chld-count] :as node}]
  (if (zero? chld-count)
    (update-top pending #(update % :children conj node))
    (conj pending node)))

(defn read-tree-node
  [[pending input]]
  (let [[node input] (mk-node input)
        [pending input] (-> pending
                            (update-top #(update % :chld-count dec))
                            (add-pending-or-to-parent node)
                            (reduce-pending-nodes input))]

    [pending input]))

(declare read-tree)

(defn read-children
  [ct input]
  (reduce
    (fn [[children input] _]
      (let [[child input] (read-tree input)]
        [(conj children child) input]))
    [[] input]
    (range ct)))

(defn mk-node
  [input]
  (let [[cc mc] (take 2 input)
        input* (drop 2 input)
        node {:children [] :metadata [] :chld-count cc :mtd-count mc}]
    [node input*]))

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

(defn parse-tree
  [input]
  (->> [[] input]
       (iterate read-tree-node)
       (drop-while (comp seq second))
       ffirst))

(defn part-b
  [input]
  (->> [[] input]
       (iterate read-tree-node)
       (drop-while (comp seq second))
       ffirst))

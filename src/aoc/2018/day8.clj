(ns aoc.2018.day8
  (:require 
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

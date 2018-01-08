(ns aoc.2017.day12
  (:require 
    [blancas.kern.core :refer :all]
    [blancas.kern.lexer.basic :refer :all]
    [clojure.tools.trace :refer [trace]]))


(def program-p
  (bind [pr dec-lit 
         _ (token "<->") 
         prs (comma-sep dec-lit)]
        (return [pr prs])))


(defn load-programs
  [input]
  (->> input
       (reduce #(apply assoc %1 (value program-p %2)) {})))

(defn collect-grp-step
  [graph {visited :visited [g & gs] :pending :as st}]
  (let [grps (graph g)
        visited (conj visited g)
        new-grps (clojure.set/difference (set grps) visited)]
    {:visited visited :pending (concat gs new-grps)}))

(defn collect-group
  [gid graph]
  (->> {:pending [gid] :visited #{}}
       (iterate (partial collect-grp-step graph))
       (drop-while (comp not empty? :pending))
       first
       :visited))

(defn count-distinct-groups
  [graph]
  (->> graph
       keys
       (map #(collect-group % graph))
       set
       count))

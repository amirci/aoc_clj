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


(defn collect-grp
  [gid graph]
  (loop [[g & gs] (graph gid) visited #{}]
    (if-not g
      visited
      (let [grps (graph g)
            visited (conj visited g)
            new-grps (clojure.set/difference (set grps) visited)
            pending (concat gs new-grps)]
        (recur pending visited)))))


(ns aoc.2018.day7
  (:require 
    [clojure.string :as str]))


(defn parse-line
  [line]
  (let [[_ a _ _ _ _ _ b] (clojure.string/split line #" ")]
    (mapv first [a b])))

(defn build-tree
  [m [a b]]
  (if (m a)
    (update m a (comp vec sort conj) b)
    (assoc m a [b])))

(defn parse
  [input]
  (->> input
       (map parse-line)
       (reduce build-tree {})))

(defn find-complete
  [[completed pending :as state]]
  (let [found (first
                (sort
                  (clojure.set/difference 
                    (set (keys pending))
                    (set (apply concat (vals pending))))))
        pending* (dissoc pending found)
        completed* (conj completed found)
        completed* (if (seq pending*) 
                    completed*
                    (concat completed* (pending found)))]
    [completed* pending*]))

(defn part-a
  [input]
  (->> input
       parse
       (vector []) 
       (iterate find-complete)
       (drop-while (comp seq second))
       first
       first
       (apply str)))


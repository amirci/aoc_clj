(ns aoc.2018.day3
  (:require 
    [clojure.string :as str]))

(defn parse-claim
  [sc]
  (read-string (str "[" sc "]")))

(defn claim->set
  [[_ x y w l]]
  (set
    (for [a (range x (+ x w)) b (range y (+ y l))]
      [a b]))) 

(defn calc-overlap
  [[claims overlap]]
  (let [c (first claims)
        claims (rest claims)]
    [claims 
     (reduce
       (fn [overlap cx]
         (clojure.set/union 
           overlap
           (clojure.set/intersection c cx)))
       overlap
       claims)])) 

;; Part A
(defn total-overlap
  [claims]
  (let [claims (map (comp claim->set parse-claim) claims)]
    (->> [claims #{}]
         (iterate calc-overlap)
         (drop-while (comp seq first))
         first
         second
         count)))

;; Part B

(defn remove-overlapping-ids
  [claims ids c1 id1]
  (reduce
    (fn [ids [id2 c2]]
      (if (empty? (clojure.set/intersection c1 c2))
        ids
        (disj ids id1 id2)))
    ids
    claims))

(defn calc-nooverlap
  [[claims ids]]
  (let [[id c] (first claims)
        claims (dissoc claims id)]
    (vector
      claims
      (remove-overlapping-ids claims ids c id))))

(defn no-overlaps
  [claims]
  (let [claims (->> claims
                    (map parse-claim)
                    (map (juxt first claim->set))
                    (into {}))]

    (->> [claims (set (map first claims))]
         (iterate calc-nooverlap)
         (drop-while #(< 1 (count (second %))))
         first
         second
         first)))


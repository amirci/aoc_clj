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
    (println ">>>> " (count claims))
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


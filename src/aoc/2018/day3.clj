(ns aoc.2018.day3
  (:require 
    [clojure.string :as str]))

(defn parse-claim
  [sc]
  (as-> sc result
    (str/replace result #"[#@,:x]" " ")
    (str "[" result "]")
    (read-string result)))

(defn claim->set
  [[_ x y w l]]
  (set
    (for [a (range x (+ x w)) b (range y (+ y l))]
      [a b]))) 

;; Part A
(defn total-overlap
  [claims]
  (let [claims (map (comp claim->set parse-claim) claims)]
    (->> (for [c1 claims c2 claims :when (not= c1 c2)]
           (clojure.set/intersection c1 c2))
         (apply clojure.set/union)
         count)))


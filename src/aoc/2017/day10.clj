(ns aoc.2017.day10
  (:require [clojure.tools.trace :refer [trace]]))

(defn rotate
  [coll n]
  (->> coll
       cycle
       (drop (- (count coll) n))
       (take (count coll))))

(defn build-hash-step
  [{:keys [nums curr skip] [ln & lns] :lengths :as st}]
  (let [ll (->> nums cycle (drop curr) (take ln) reverse)
        dif (- (count nums) ln)
        lr (->> nums cycle (drop (+ curr ln)) (take dif))]
    {:nums (rotate (concat ll lr) curr)
     :lengths lns
     :curr (mod (+ curr ln skip) (count nums))
     :skip (inc skip)}))


(defn build-hash
  [m]
  (->> m
       (iterate build-hash-step)
       (drop-while :lengths)
       first))


(defn first-two
  [lens]
  (->> {:nums (range 256) :curr 0 :skip 0 :lengths lens}
       build-hash
       :nums
       (take 2)
       (apply *)))

(defn full-run
  [lens m]
  (-> m
      (assoc :nums (range 256) :lengths lens)
      build-hash
      trace))

(defn knot
  [input]
  (let [lens (->> input (map int))
        fr (partial full-run (concat lens [17 31 73 47 23]))]
    (->> {:curr 0 :skip 0}
         (iterate fr)
         (drop 3)
         first
         :nums
         (partition 16)
         (map (partial apply bit-xor))
         (map (partial format "%02x"))
         clojure.string/join)))

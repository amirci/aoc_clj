(ns aoc.2017.day10
  (:require
    [blancas.kern.core :refer :all]
    [blancas.kern.lexer.basic :refer :all]))

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
  ([lens] (build-hash (range 256) lens))
  ([nums lens]
   (->> {:nums nums
         :lengths lens
         :curr 0
         :skip 0}
        (iterate build-hash-step)
        (drop-while :lengths)
        first
        :nums
        (take 2)
        (apply *))))

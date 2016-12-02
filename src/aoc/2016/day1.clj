(ns aoc.2016.day1
  (:require [clojure.tools.trace :refer [trace]]))

(defn parse
  [in]
  (let [n (Integer. (.substring in 1))
        lor (keyword (.substring in 0 1))]
    [lor n]))

(defn abs [n] (if (neg? n) (- n) n))

(defn mk-map [r l] {:R r :L l})

(defn from-dir
  [dir lor n]
  (let [nn (- n)
        e [[n 0] :east] w [[nn 0] :west]
        nr [[0  n] :north] s [[0 nn] :south]]
    (-> {:north (mk-map e w)
         :south (mk-map w e)
         :west (mk-map nr s)
         :east (mk-map s nr)}
        dir
        lor)))

(defn distance
  [[dir pt] [lor n]]
  (let [[new-pt new-dir] (from-dir dir lor n)]
    [new-dir (map + pt new-pt)]))

(defn blocks-away
  [instructions]
  (->> instructions
       (map parse)
       (reduce distance [:north [0 0]])
       last
       (map abs)
       (reduce +)))

(defn neg-range
  [start end]
  (if (> start end) (reverse (drop 1 (range end (inc start)))) (range start end)))

(defn points
  [[x1 y1] [x2 y2]]
  (if (= x1 x2)
    (for [i (neg-range y1 y2)] [x1 i])
    (for [i (neg-range x1 x2)] [i  y1])))

(defn find-intersection
  ([points] (find-intersection (set (take 1 points)) (drop 1 points)))
  ([visited [current & rst]]
   (when current
     (if-let [found (visited current)]
       found
       (find-intersection (conj visited current) rst)))))

(defn first-repeated
  [instructions]
  (let [all-pts (->> instructions
                     (map parse)
                     (reductions distance [:north [0 0]])
                     (map last))
        expanded (mapcat points all-pts (drop 1 all-pts))]
    (->> (find-intersection expanded)
         (map abs)
         (reduce +))))



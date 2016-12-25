(ns aoc.2016.day19
  (:require [clojure.tools.trace :refer [trace]]
            [digest :refer :all]))


(def nbr-elves 3014603)

(defn josephus
  [elves]
  (-> elves
      (Integer/toString 2)
      (as-> bin (concat (rest bin) (str (first bin))))
      (as-> s (apply str s))
      (Integer/parseInt 2)))

(def white-elephant josephus)

; part b
(defn next-alive
  [coll start]
  (let [n (count coll)]
    (loop [i (mod (inc start) n)]
      (let [x (nth coll i)]
        (if (pos? x)
          i
          (recur (mod (inc i) n)))))))

(defn kill-across
  [[elves last-killed times]]
  (let [to-kill (next-alive elves last-killed)
        to-kill (if (even? (- (count elves) times)) (next-alive elves to-kill) to-kill)]
    [(assoc elves to-kill 0) to-kill (inc times)]))


(defn white-elephant-2
  [elves]
  (let [coll (vec (range 1 (inc elves)))
        tgt (int (Math/floor (/ elves 2)))]
    (->> [(assoc coll tgt 0) tgt 1]
         (iterate kill-across)
         (drop-while #(not= (last %) (dec elves)))
         first
         first
         (filter pos?)
         first)))

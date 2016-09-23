(ns aoc.day7
  (:require [clojure.string :as string]
            [clojure.edn :as edn]))

(def empty-board [{} []])

(defn circuit 
  [[board pending] c]
  (get board c))

(defn parse-int
  [s]
  (let [parsed (read-string s)]
       (when (integer? parsed) parsed)))

(defn parse
  [[board pending] instruction]
  (let [[t1 t2 t3 t4 t5] (clojure.string/split instruction #"\ +")
        [v1 v2 v3] (map #(or (parse-int %) (get board %)) [t1 t2 t3])
        [n1 n3] (map read-string [t1 t3])
        parsed (cond
                  (= t2 "->"    ) (when n1 (assoc board t3 n1))
                  (= t2 "AND"   ) (when (and v1 v3) (assoc board t5 (bit-and v1 v3)))
                  (= t2 "OR"    ) (when (and v1 v3) (assoc board t5 (bit-or v1 v3)))
                  (= t2 "LSHIFT") (when v1 (assoc board t5 (bit-shift-left v1 n3)))
                  (= t2 "RSHIFT") (when v1 (assoc board t5 (bit-shift-right v1 n3)))
                  (= t1 "NOT"   ) (when v2 (assoc board t4 (bit-and 0xffff (bit-not v2)))))]
          (if parsed [parsed pending] [board (conj pending instruction)])))


(defn parse-all
  [[board instructions]] 
  (let [[new-board new-inst] (reduce parse [board []] instructions)]
       (when (not= (set instructions) (set new-inst))
             [new-board new-inst])))

(defn still-pending? [[_ pending]] (> (count pending) 0))

(defn mk-board
  [instructions]
  (->> [{} instructions]
       (iterate parse-all)
       (drop-while still-pending?)
       first
       first))



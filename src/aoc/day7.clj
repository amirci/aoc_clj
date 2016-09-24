(ns aoc.day7
  (:require [clojure.string :as string]
            [clojure.edn :as edn]))

(def empty-board [{} []])

(defn circuit 
  [[board pending] c]
  (get board c))

(defn try-parse-int
  [s]
  (let [parsed (read-string s)]
       (when (integer? parsed) parsed)))

(def tokenize #(clojure.string/split % #"\ +"))

(defn parse
  [[board pending] instruction]
  (let [[t1 t2 t3 t4 t5] (tokenize instruction)
        [v1 v2 v3] (map #(or (try-parse-int %) (get board %)) [t1 t2 t3])
        parsed (cond
                  (= t2 "->"    ) (when v1 (assoc board t3 v1))
                  (= t2 "AND"   ) (when (and v1 v3) (assoc board t5 (bit-and v1 v3)))
                  (= t2 "OR"    ) (when (and v1 v3) (assoc board t5 (bit-or v1 v3)))
                  (= t2 "LSHIFT") (when v1 (assoc board t5 (bit-shift-left v1 v3)))
                  (= t2 "RSHIFT") (when v1 (assoc board t5 (bit-shift-right v1 v3)))
                  (= t1 "NOT"   ) (when v2 (assoc board t4 (bit-and 0xffff (bit-not v2)))))]
          (if parsed [parsed pending] [board (conj pending instruction)])))


(defn parse-all
  [[board instructions]]
  (let [[new-board new-inst] (reduce parse [board []] instructions)]
       (when (not= (set instructions) (set new-inst))
             [new-board new-inst])))

(defn still-pending? [[_ pending]] (> (count pending) 0))

(defn load-board
  [instructions]
  (->> [{} instructions]
       (iterate parse-all)
       (drop-while still-pending?)
       first
       first))



(ns aoc.day7
  (:require [clojure.string :as string]
            [clojure.edn :as edn]))

(defn parse
  [board instruction]
  (let [[t1 t2 t3 t4 t5] (clojure.string/split instruction #"\ +")
        [v1 v2 v3] (map #(get board %) [t1 t2 t3])
        [n1 n3] (map read-string [t1 t3])]
        (cond
          (= t2 "->"    ) (assoc board t3 n1)
          (= t2 "AND"   ) (assoc board t5 (bit-and v1 v3))
          (= t2 "OR"    ) (assoc board t5 (bit-or v1 v3))
          (= t2 "LSHIFT") (assoc board t5 (bit-shift-left v1 n3))
          (= t2 "RSHIFT") (assoc board t5 (bit-shift-right v1 n3))
          (= t1 "NOT"   ) (assoc board t4 (bit-and 0xffff (bit-not v2)))
          :else board)))

(defn mk-board
  [instructions]
  (reduce parse {} instructions))



(ns aoc.2020.day14
  (:require [clojure.string :as s]
            [clojure.pprint :refer (cl-format)]))


(defn parse-assignment [ass]
  (->> ass
       (re-matches #"mem\[(\d+)\] = (\d+)")
       (drop 1)
       (map clojure.edn/read-string)
       vec))

(defn parse [[[mask] asss]]
  {:mask (subs mask 7)
   :asss (map parse-assignment asss)})

(defn mask-bit [m c]
  (if (= \X m) c m))

(def ->36b (partial cl-format nil "~36,'0',B"))

(defn read-bs [bs]
  (->> bs
       (cons "2r")
       (apply str)
       clojure.edn/read-string))

(defn apply-mask [nbr mask]
  (->> nbr
       ->36b
       (map mask-bit mask)
       read-bs))

(defn assign [mem {:keys [mask asss]}]
  (->> asss
       (map #(update % 1 apply-mask mask))
       (into {})
       (merge mem)))

(defn load-inst [input]
  (->> input
       (partition-by #(s/starts-with? % "mask"))
       (partition 2)
       (map parse)))

(defn sum-memory
  ([input] (sum-memory assign input))
  ([f input]
   (->> input
        load-inst
        (reduce f {})
        vals
        (apply +))))


(defn bit-mask-v2 [m a]
  (cond
    (= m \0) a
    (= m \1) 1
    :else   m))

(defn addr-mask [mask addr]
  (->> addr
       ->36b
       (map bit-mask-v2 mask)))

(defn floating [amsk]
  (->> amsk
       (map-indexed vector)
       (filter (comp (partial = \X) second))
       (map first)))

(defn address-range [flt]
  (let [fmt (format "~%d,'0',B" (count flt))]
    (->> flt
         count
         (Math/pow 2)
         range
         (map (partial cl-format nil fmt)))))

(defn ->address [amsk flt bn]
  (->> bn
       (map vector flt)
       (reduce (partial apply assoc) (vec amsk))
       (apply str)))

(defn apply-mask-v2 [mask [addr nbr]]
  (let [amsk (addr-mask mask addr)
        flt (floating amsk)]
    (->> flt
         address-range
         (map (partial ->address amsk flt))
         (map read-bs)
         (map #(vector % nbr))
         (into {}))))

(defn assign-v2 [mem {:keys [mask asss]}]
  (->> asss
       (map (partial apply-mask-v2 mask))
       (apply merge mem)))

(def sum-memory-v2 (partial sum-memory assign-v2))

(ns aoc.2021.day16
  (:require [clojure.string :as st]))


(defn ->dec
  [binary]
  (Long/parseLong (apply str binary) 2))

(declare parse-packets-raw)


(declare parse-packet)




(defmulti parse-operator first)


(defmethod parse-operator \0
  [[_ & rst]]
  (if (< (count rst) 15)
    {:packet {:error rst}}
    (let [[length pending] (split-at 15 rst)
          length (->dec length)
          [sub-packets pending] (split-at length pending)]
      {:packet {:type :operator
                :packets (parse-packets-raw sub-packets)}
       :pending pending})))


(defn parse-n-packets
  [n pending]
  (->> {:pending pending}
       (iterate parse-packet)
       (drop 1)
       (take n)
       ((juxt (partial map :packet) (comp :pending last)))))


(defmethod parse-operator \1
  [[_ & rst]]
  (let [[nbr pending] (split-at 11 rst)
        nbr (->dec nbr)
        [packets pending] (parse-n-packets nbr pending)]
    {:packet {:type :operator
              :sub-packets-nbr nbr
              :packets packets}
     :pending pending}))


(defmulti parse-body (comp :type-id :packet))


(defn read-literal-value
  [pending]
  (loop [nbr [] size 0 pending pending]
    (let [[[fst :as part] pending] (split-at 5 pending)
          nbr (into nbr (rest part))
          size (+ 5 size)]
      (if (= \0 fst)
        [nbr size pending]
        (recur nbr size pending)))))


(defmethod parse-body 4
  [{:keys [pending] :as input}]
  (let [[literal _ pending] (read-literal-value pending)]
    (-> input
        (update :packet merge {:type :literal-value
                               :value (->dec literal)
                               :literal literal})
        (assoc :pending pending))))

(defn bool->num
  [b]
  (if b 1 0))

(def operators
  {0 +
   1 *
   2 min
   3 max
   5 (comp bool->num >)
   6 (comp bool->num <)
   7 (comp bool->num =)})

(defn eval-packet
  [{{:keys [packets type-id]} :packet :as state}]
  (->> packets
       (map :value)
       (remove nil?)
       (apply (operators type-id))
       (assoc-in state [:packet :value])))

(defmethod parse-body :default
  [{:keys [packet pending]}]
  (-> pending
      parse-operator
      (update :packet merge packet)
      (eval-packet)))


(defn parse-packet
  [{:keys [pending] :as input}]
  (cond
    (empty? pending) {:pending nil}
    (<= (count pending) 6) {:pending [] :packet {:error pending}}
    :else (let [[header pending] (split-at 6 pending)
                [version type-id] (map ->dec (split-at 3 header))]
            (-> input
                (assoc :pending pending)
                (assoc :packet {:version version :type-id type-id})
                parse-body))))


(defn ->binary
  [n]
  (->> n
       (Integer/toBinaryString)
       (format "%4s")
       (#(st/replace % \  \0))))


(defn binary
  [nbrs]
  (->> nbrs
       (map (partial str "0x"))
       (map read-string)
       (mapcat ->binary)))


(defn parse-packets-raw
  ([binary] (parse-packets-raw (partial take-while :pending) binary))
  ([stop-fn binary]
   (->> {:pending binary}
        (iterate parse-packet)
        (drop 1)
        stop-fn
        (map :packet))))


(defn parse-packets
  [nbrs]
  (->> nbrs
       binary
       parse-packets-raw))


(defn sum-versions
  [nbrs]
  (->> nbrs
       parse-packets
       (hash-map :version 0 :type :root :packets)
       (tree-seq :packets :packets)
       (map :version)
       (remove nil?)
       (apply +)))


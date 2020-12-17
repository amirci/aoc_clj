(ns aoc.2020.day16
  (:require
   [blancas.kern.core :as kc :refer :all]
   [blancas.kern.lexer.java-style :as lx]))

(def range-parser
  (<$>
   (fn [[n1 _ n2]] (range n1 (inc n2)))
   (<*> dec-num (lx/sym \-) dec-num)))

(def ranges-parser
  (<$>
   (fn [[r1 _ r2]] (clojure.set/union (set r1) (set r2 )))
   (<*> range-parser (skip-ws (lx/word "or")) range-parser)))

(def field-parser
  (<$>
   (fn [[field _ rngs]] {:field field :rngs rngs})
   (<*> (lx/field ":") lx/colon ranges-parser)))


(defn union-ranges [fields]
  (->> fields
       (map :rngs)
       (apply clojure.set/union)))

(defn parse-field [input]
  (-> (parse field-parser input)
      :value))

(def parse-fields (partial map parse-field))

(defn parse-nbr-list [coll]
  (->> coll
       (drop 1)
       (map (partial parse (lx/comma-sep dec-num)))
       (map :value)))

(defn parse-input [input]
  (->> input
       (partition-by empty?)
       (remove (comp empty? first))
       (map #(%1 %2) [parse-fields
                      (comp first parse-nbr-list)
                      parse-nbr-list])
       (map vector [:fields :ticket :nearby])
       (into {})
       (#(assoc % :valid (union-ranges (:fields %))))))

(defn find-invalid [input]
  (let [{:keys [valid fields nearby]} (parse-input input)]
    (->> nearby
         (map set)
         (remove #(clojure.set/subset? % valid))
         (mapcat #(clojure.set/difference % valid))
         (apply +))))

(defn matching [ts pending]
  (filter
   #(clojure.set/subset? ts (:rngs %))
   pending))

(defn discover-field
  [{:keys [pending ticket result] :as state}]

  (let [[t [field]] (->> ticket
                         (map (fn [[t ts]] [t (matching ts pending)]))
                         (filter (fn [[t fields]] (= 1 (count fields))))
                         first)]
    (assert field)
    (-> state
        (update :pending disj field)
        (update :result assoc (:field field) t))))

(def transpose #(apply map vector %))

(defn discover-fields [field-rules ticket-fields]
  (->> ticket-fields
       (assoc {:pending (set field-rules) :result {}} :ticket)
       (iterate discover-field)
       (drop-while (comp seq :pending))
       first
       :result))

(defn departures [input]
  (let [{:keys [valid fields nearby ticket]} (parse-input input)]
    (->> nearby
         (filter #(clojure.set/subset? (set %) valid))
         transpose
         (map set)
         (map vector ticket)
         (discover-fields fields))))


(defn mul-departures [input]
  (->> input
       departures
       (filter #(clojure.string/starts-with? (first %) "departure"))
       (map second)
       (apply *)))

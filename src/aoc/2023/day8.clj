(ns aoc.2023.day8
  (:require [blancas.kern.core :as kc :refer [skip-ws <$> many1]]
            [clojure.string :as s]
            [clojure.math.numeric-tower :as math]
            [blancas.kern.lexer.basic :as lex :refer [comma-sep1 parens]]))

(def p-node
  (<$> (partial apply str)
       (many1 (lex/one-of "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"))))

(def p-id-pair
  (parens (comma-sep1 p-node)))


(def p-line
  (kc/bind [id p-node
            _ (lex/sym \=)
            id-pair (skip-ws p-id-pair )]
           (kc/return [id id-pair])))

(defn parse-mappings [mappings]
  (->> mappings
       (map (partial kc/value p-line))
       (into {})))


(defn as-zero-ones [moves]
  (->> moves
       (map (fn [move] (if (= \R move) 1 0)))))


(defn steps-to-terminus
  ([moves node-map] (steps-to-terminus moves node-map (partial = "ZZZ") "AAA"))
  ([moves node-map terminus? starter]
   (->> moves
        cycle
        (reduce
         (fn [[counter fst lst] mv]
           (if (terminus? lst)
             (reduced [counter fst lst])
             [(inc counter) fst (get-in node-map [lst mv])]))
         [0 starter starter])
        first)))

(defn nodes-that-end-with [tgt node-map]
  (->> node-map
       (filter (fn [[node]] (s/ends-with? node tgt)))
       (map first)))

(defn all-states-final? [states]
  (->> states
       (map last)
       (every? #(s/ends-with? % "Z"))))


(defn add-last-steps [states node-map move]
  (for [state states]
    (assoc state 1 (get (node-map (last state)) move))))


(defn find-ending-for-each-starter [moves node-map]
  (->> node-map
       (nodes-that-end-with "A")
       (pmap (partial steps-to-terminus (as-zero-ones moves) node-map #(s/ends-with? % "Z")))))


(defn count-steps-to-terminus [[moves _ & mappings]]
  (->> mappings
       parse-mappings
       (steps-to-terminus (as-zero-ones moves))))

(defn count-ghost-steps-to-terminus [[moves _ & mappings]]
  (->> mappings
       parse-mappings
       (find-ending-for-each-starter moves)
       (reduce (fn [gcd n] (math/lcm gcd n)))))

(ns aoc.2022.day4
  (:require [clojure.set :as cst]
            [blancas.kern.core :as kc]
            [blancas.kern.lexer.basic :as lex]))


(def p-range
  (kc/bind [start lex/dec-lit _ (lex/sym \-) end lex/dec-lit]
           (kc/return (range start (inc end)))))

(def p-range-pair
  (kc/bind [r1 p-range _ lex/comma r2 p-range]
           (kc/return [r1 r2])))

(defn parse-ranges [line]
  (kc/value p-range-pair line))


(defn- included-range? [rs]
  (let [[s1 s2] (map set rs)]
    (or (cst/subset? s1 s2)
        (cst/subset? s2 s1))))

(defn count-included-ranges [pairs]
  (->> pairs
       (filter included-range?)
       count))

(defn- overlap-range? [pair]
  (let [[s1 s2] (map set pair)]
    (seq (cst/intersection s1 s2))))

(defn count-overlap-ranges [pairs]
  (->> pairs
       (filter overlap-range?)
       count))

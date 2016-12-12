(ns aoc.2016.day12
  (:require [clojure.tools.trace :refer [trace]]
            [clojure.string :refer [join starts-with? split]]))

(def to-int #(Integer. %))

(defmulti parse first)

(defmethod parse "cpy"
  [[_ n reg]]
   (fn [registers]
     (let [v (if (re-matches #"[a-z]" n) (registers n) (to-int n))]
       [inc (assoc registers reg v)])))

(defmethod parse "inc"
  [[_ reg]]
   (fn [{n reg :as registers}] 
     [inc (assoc registers reg (inc n))]))

(defmethod parse "dec"
  [[_ reg]]
   (fn [{n reg :as registers}] 
     [inc (assoc registers reg (dec n))]))

(defmethod parse "jnz"
  [[_ reg n]]
   (fn [{r reg :as registers}]
     (if (not= r 0)
       [(partial + (to-int n)) registers]
       [inc registers])))

(def parse-memo (memoize parse))

(defn eval-instruction
  [[ptr registers cmds]]
  (if (< ptr (count cmds))
    (let [f (parse-memo (nth cmds ptr))
          [f-ptr registers] (f registers)]
      (trace ">>>>>> " [ptr (nth cmds ptr) registers])
      [(trace "----- " (f-ptr ptr)) registers cmds])
    [ptr registers cmds]))

(def evaluations #(iterate eval-instruction [0 {"a" 0 "b" 0 "c" 0 "d" 0} %]))

(defn valid-ptr
  [[ptr _ cmds]]
   (< ptr (count cmds)))

(defn run-assembunny
  [cmds]
  (->> cmds
       (map #(split % #" "))
       evaluations
       ;(drop 100)
       (drop-while valid-ptr)
       first
       (drop 1)
       first))

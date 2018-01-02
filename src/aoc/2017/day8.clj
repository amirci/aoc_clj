(ns aoc.2017.day8
  (:require 
    [blancas.kern.core :refer :all]
    [blancas.kern.lexer.basic :refer :all]))


(def cmp-p (<$> (comp deref resolve read-string)
                (token ">=" "<=" "<" ">" "==" "not=")))

(def int-p (<$> read-string (<+> (optional (token "-")) dec-num)))

(def reg-p (<$> (comp keyword (partial apply str)) (many1 letter)))

(defn update-if
  [op k1 v1 cmp k2 v2 m]
  (let [r2 (get m k2 0)]
    (if (cmp r2 v2)
      (update m k1 #(op (or % 0) v1))
      m)))

(defn op-parser
  [tk op]
  (bind [k1 reg-p _ space _ (token tk) i1 int-p _ space
         _ (token "if")
         k2 reg-p _ space cmp cmp-p i2 int-p]
        (return (partial update-if op k1 i1 cmp k2 i2))))

(def inc-p (op-parser "inc" +))

(def dec-p (op-parser "dec" -))

(def inst-p (<|> (<:> inc-p) dec-p))

(defn inst-parser
  [line]
  (value inst-p (clojure.string/replace line "!=" "not=")))

(defn fmap [m f] (f m))

(defn max-value
  [inst]
  (->> inst
       (map inst-parser)
       (reduce fmap {})
       vals
       (apply max)))

(defn max-stored-value
  [inst]
  (->> inst
       (map inst-parser)
       (reductions fmap {})
       (drop-while empty?)
       (map (comp (partial apply max) vals))
       (apply max)))




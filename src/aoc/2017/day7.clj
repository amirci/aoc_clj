(ns aoc.2017.day7
  (:require 
    [blancas.kern.core :refer :all]
    [blancas.kern.lexer.basic :refer :all]))


;; Parsing

(def program-name-p (<?> (<$> #(keyword (apply str %)) (many1 letter)) "valid program name"))

(def program-val-p (<*> program-name-p (skip-ws (parens dec-num))))

(def dep-list-p (sep-by comma program-name-p))

(def assign-p (>> (skip-ws (token "->")) dep-list-p))

(def program-line-p
  (bind [prg  program-val-p
         deps (optional assign-p)]
        (return (conj prg (or deps [])))))

;;;;;;

(defn collect-words
  [words line]
  (-> (value program-line-p line)
      (as-> [p _ ps]
        (apply conj words p ps))))

(defn find-root
  [lines]
  (->> lines
       (reduce collect-words [])
       frequencies
       (filter (fn [[k v]] (= v 1)))
       ffirst))

(defn found-in-deps [tw] (->> tw (filter first) first))

(defn sum-tower [pw tw] [false (apply + pw (map second tw))])

(defn is-in-the-tower
  [fr]
  (when (> (count fr) 2)
    [true ]
    ))

(defn find-unbalance*
  [ws brs prg]
  (let [prg-weight (prg ws)
        tower (or (prg brs) [])
        tower-weights (map find-unbalance* tower)
        freq (->> tower-weights (map second) frequencies)]
    (or
      (found-in-deps tower-weights)
      (is-in-the-tower freq)
      (sum-tower prg-weight tower-weights))))

(defn find-branch
  [b tree]
  (->> tree
       (filter (comp (partial = b) first))
       first))

(defn disc-diff
  [tree same dif child]
  (-> child
      (find-branch tree) 
      second 
      (- dif)
      (+ same)))

(defn children-difference
  [tree children cws]
  (let [[[_ dif] [_ same]] (->> cws frequencies clojure.set/map-invert sort)]
    (when same
      (->> cws 
           (map vector children) 
           (filter (comp (partial = dif) second))
           ffirst
           (disc-diff tree same dif)))))

(defn calc-branch-weight
  [tree weights branch]
  (let [[_ w children] (find-branch branch tree)
        weights (reduce (partial calc-branch-weight tree) weights children)]
    (if (integer? weights)
      weights
      (let [cws (map weights children)
            dif (children-difference tree children cws)]
        (if dif
          (reduced dif)
          (assoc weights
                 branch
                 (apply + w cws)))))))

(defn find-unbalanced-weight
  [lines]
  (let [tree (map (partial value program-line-p) lines)
        root (find-root lines)]
    (reduce (partial calc-branch-weight tree) {} [root])))

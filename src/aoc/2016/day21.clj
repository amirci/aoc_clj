(ns aoc.2016.day21
  (:require [clojure.tools.trace :refer [trace]]
            [clojure.math.combinatorics :as combo]
            [clojure.string :refer [split join index-of replace]]))

(defmulti swap first)
(defmulti run first)
(defmulti rotate first)

; swap position X with position Y
(defmethod swap "position"
  [[_ x _ _ y word]]
  (let [x (Integer. x) y (Integer. y)
        oldx (nth word x) oldy (nth word y)]
    (-> word
        vec
        (assoc x oldy)
        (assoc y oldx)
        (as-> v (apply str v)))))

(defn swap-letter
  [l1 l2]
  (fn [c]
    (cond
      (= c l1) l2
      (= c l2) l1
      :else c)))

; swap letter X with letter Y
(defmethod swap "letter"
  [[_ l1 _ _ l2 word]]
  (->> word
       (map (swap-letter (first l1) (first l2)))
       (apply str)))

(defmethod run "swap" [[_ & rst]] (swap rst))

; reverse positions X through Y
(defmethod run "reverse"
  [[_ _ x _ y word]]
  (let [x (Integer. x) y (Integer. y)
        beg (subs word 0 x)
        middle (apply str (reverse (subs word x (inc y))))
        end (subs word (inc y))]
    (str beg middle end)))

(defn rotate-left [word n] (concat (drop n word) (take n word)))

(defn rotate-right [word n] (rotate-left word (- (count word) n)))

(defmethod rotate "left"
  [[_ n _ word]]
  (let [n (Integer. n)]
    (apply str (rotate-left word n))))

(defmethod rotate "right"
  [[_ n _ word]]
  (let [n (Integer. n)]
    (apply str (rotate-right word n))))

(defmethod rotate "based"
  [[_ _ _ _ _ l word :as cmd]]
  (let [i (index-of word l)
        i (if (> i 3) (inc i) i)
        i (inc i)
        i (mod i (count word))]
    (apply str (rotate-right word i))))

(defmethod run "rotate" [[_ & rst]] (rotate rst))

(defn remove-at
  [word pos]
  (let [beg (subs word 0 pos)
        rst (subs word (inc pos))]
    (str beg rst)))

(defn insert-at
  [word pos l]
  (let [beg (subs word 0 pos)
        rst (subs word pos)]
    (str beg l rst)))

(defmethod run "move"
  [[_ _ x _ _ y word]]
  (let [[x y] [(Integer. x) (Integer. y)]
        letter (nth word x)]
    (-> word
        (remove-at x)
        (insert-at y letter))))

(defn instruction
  [word cmd]
  (-> cmd (split #" ") (conj word) run))

(defn scramble
  [word instructions]
  (reduce instruction word instructions))

; part b
(defn unscramble
  [word instructions]
  (->> (combo/permutations "abcdefgh")
       (filter #(= (scramble % instructions) word))
       first
       (apply str)))

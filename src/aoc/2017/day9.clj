(ns aoc.2017.day9
  (:require
    [blancas.kern.core :refer :all]
    [blancas.kern.lexer.basic :refer :all]))

(def begin-group (sym \{))
(def end-group   (sym \}))


(defn remove-escaped
  [input]
  (loop [parsed "" left input]
    (let [[a b] (split-with (partial not= \!) left)]
      (if (empty? b)
        (apply str parsed a)
        (recur (apply str parsed a) (drop 2 b))))))

(def garbage
  (bind [_ (sym \<)
         _ (many-till any-char (sym \>))]
        (return 0)))

(defn group
  [level]
  (bind [_ (sym \{)
         v (comma-sep (<|> (group (inc level)) garbage))
         _ (sym \})]
        (return (apply + level v))))

(defn group-score
  [s]
  (->> s
       remove-escaped
       (value (group 1))))

(def garbage*
  (bind [_ (sym \<)
         t (many-till any-char (sym \>))]
        (return (count t))))

(def group*
  (bind [_ (sym \{)
         v (comma-sep (<|> group* garbage*))
         _ (sym \})]
        (return (apply + v))))

(defn count-garbage
  [s]
  (->> s
       remove-escaped
       (value group*)))

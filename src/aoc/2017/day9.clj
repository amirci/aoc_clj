(ns aoc.2017.day9
  (:require
    [blancas.kern.core :refer :all]
    [blancas.kern.lexer.basic :refer :all]))

(def begin-group (sym \{))
(def end-group   (sym \}))

(declare group)

(def garbage
  (bind [_ (sym \<)
         ls (many-till any-char (sym \>))
         _ (sym \>)]))
  ; read garbage until group starts
  ; read garbage until group starts or ends

(def group
  (bind [_ begin-group
         v (comma-sep (<|> group garbage))
         _ end-group]
        (return (apply + 1 v))))
  

(defn group-score
  ([input] (value (group 0) input)))

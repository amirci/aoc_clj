(ns aoc.2017.day16
  (:require
   [blancas.kern.core :refer [value <$> >> <|> dec-num sep-by letter]]
   [blancas.kern.lexer.java-style :as lx]))


(defn spin [n programs]
  (->> programs
       cycle
       (drop (- (count programs) n))
       (take (count programs))
       vec))

(def spin-expr
  (<$> #(partial spin %)
       (>> (lx/sym \s) dec-num)))


(defn exchange [a b programs]
  (-> programs
      (assoc a (programs b))
      (assoc b (programs a))))


(defn sep-expr [f prefix parser]
  (<$> (fn [[a b]] (partial f a b))
       (>> prefix (sep-by (lx/sym \/) parser))))


(def exchange-expr
  (sep-expr exchange (lx/sym \x) dec-num))


(defn swap [a b programs]
  (let [idx1 (.indexOf programs a)
        idx2 (.indexOf programs b)]
    (exchange idx1 idx2 programs)))


(def swap-expr
  (sep-expr swap (lx/sym \p) letter))


(def move-expr
  (<|> swap-expr exchange-expr spin-expr))


(defn dance* [moves programs]
  (reduce #(%2 %1) (vec programs) moves))


(defn dance
  "Cycle repeats after 48 rounds for full a-p"
  ([programs moves] (dance 1 programs moves))
  ([rounds programs moves]
   (let [moves (map (partial value move-expr) moves)
         rounds (mod rounds 48)]
     (->> programs
          (iterate (partial dance* moves))
          (drop rounds)
          first
          (apply str)))))


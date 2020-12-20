(ns aoc.2020.day19
  (:require
   [blancas.kern.core :as kc :refer :all]
   [blancas.kern.expr :as ke :refer :all]
   [blancas.kern.lexer.java-style :as lx]))


(def match-expr (<$> lx/token lx/string-lit))

(def ref-expr
  (<$> (fn [nbr]
         (bind [rules get-state
                r (rules nbr)]
               (return r)))
       lx/dec-lit))


(def ref-list-expr
  (<$> (fn [reflist]
         (<:> (apply <+> reflist)))
       (skip-ws (many ref-expr))))


(def or-expr
  (<$> (fn [refs]
         (if (= 1 (count refs))
           (first refs)
           (apply <|> refs)))
       (sep-by (sym* \|) ref-list-expr)))


(def rule-body-expr
  (<|> match-expr or-expr))


(def rule-id-expr (<< lx/dec-lit lx/colon))


(def rule-expr
  (<*> rule-id-expr rule-body-expr))


(defn parse-rules [rules]
  (->> rules
       (map (partial value rule-expr))
       (into {})))


(defn matching? [rules line]
  (->> rules
       (value (rules 0) line "aoc")
       (= line)))


(defn find-matching* [[rules input]]
  (->> input
       (filter (partial matching? rules))
       count))


(defn find-matching [input]
  (-> input
      (update 0 parse-rules)
      find-matching*))


(def updated-expr
  "Checks a sequence of 41+ followed by 31+ where the
  amount of 41 is greater than the amount of 31"
  (<$> (fn [[s1 s2 :as match]]
         (let [[c1 c2] (map count match)]
           (when (< c2 c1)
             (clojure.string/join (flatten [ s1 s2 ])))))
       (<*> (many1 (value ref-expr "42"))
            (many1 (value ref-expr "31")))))

(def new-rules
  "8: 42 | 42 8 same as 42+
  11: 42 31 | 42 11 31 same as 42 (42 31)* 31
  Because rule 0 is `8 11` it becomes a 42+31+ expression
  where the amount of 42s matches has to be higher than 31s"
  {0 updated-expr})


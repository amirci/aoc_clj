(ns aoc.2020.day18
  (:require
   [blancas.kern.core :as kc :refer :all]
   [blancas.kern.expr :as ke :refer :all]
   [blancas.kern.lexer.java-style :as lx]))

(declare math-expr)

(def factor
  (<|> lx/dec-lit (lx/parens (fwd math-expr))))

(def math-expr
  (chainl1 factor (<|> add-op mul-op)))

(defn sum-math
  ([input] (sum-math (partial value math-expr) input))
  ([eval-fn input]
   (->> input
        (map eval-fn)
        (apply +))))

(declare advanced-math-expr)

(def factor-adv
  (<|> lx/dec-lit (lx/parens (fwd advanced-math-expr))))

(def add-expr
  (chainl1 factor-adv add-op))

(def mul-expr
  (chainl1 add-expr mul-op))

(def advanced-math-expr
  (<|> mul-expr add-expr))

(def run-eval-advanced (partial value advanced-math-expr))

(def sum-math-advanced (partial sum-math run-eval-advanced))

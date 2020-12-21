(ns aoc.2020.day21
  (:require
   [blancas.kern.core :as kc :refer :all]
   [blancas.kern.expr :as ke :refer :all]
   [blancas.kern.lexer.java-style :as lx]))


(def ingredients-exp
  (<$> set (many1 lx/identifier)))

(def allergens-expr
  (<$> set
       (lx/parens
        (>> (lx/word "contains")
            (lx/comma-sep lx/identifier)))))

(def line-expr
  (<*> ingredients-exp allergens-expr))

(def parse-ingr (partial value line-expr))

(defn has-allergen? [allergen [ingr allergens]]
  (allergens allergen))

(defn find-common [ings allergen]
  (->> ings
       (filter (partial has-allergen? allergen))
       (map first)
       (apply clojure.set/intersection)))

(defn remove-found [ings allergen food]
  (map
   #(-> %
        (update 0 disj food)
        (update 1 disj allergen))
   ings))

(defn update-match [state allergen found]
  (-> state
      (update :allergens assoc allergen found)
      (update :ings remove-found allergen found)))

(defn find-source
  [{:keys [allergens ings] :as state} allergen]
  (let [[found & rst] (find-common ings allergen)]
    (if rst
      state
      (update-match state allergen found))))

(defn non-allergen [{:keys [ings] :as state}]
  (->> ings
       (mapcat second)
       set
       (reduce find-source state)))

(defn pending-allergens? [{:keys [ings]}]
  (->> ings
       (map second)
       (apply clojure.set/union)
       seq))

(defn identify-allergens [input]
  (->> input
       (map parse-ingr)
       (assoc {:allergens {}} :ings)
       (iterate non-allergen)
       (drop-while pending-allergens?)
       first))

(defn times-non-allergen [input]
  (->> input
       identify-allergens
       :ings
       (mapcat first)
       frequencies
       vals
       (apply +)))


(defn canonical-list [input]
  (->> input
       identify-allergens
       :allergens
       sort
       (map second)
       (clojure.string/join ",")))

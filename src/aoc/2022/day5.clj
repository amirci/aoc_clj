(ns aoc.2022.day5
  (:require [blancas.kern.core :as kc]
            [blancas.kern.lexer.basic :as lex]))

(def p-letter kc/any-char)

(def p-stack
  (kc/between (lex/sym \[) (lex/sym \]) p-letter))

(defn- parse-line [line]
  (kc/value p-stack line))

(defn- parse-stacks [stacks]
  (->> stacks
       drop-last
       (map (partial partition-all 4))
       (map (partial map parse-line))
       (apply map vector)
       (map (partial keep identity))
       (map (comp vec reverse))
       vec))

(def top last)

(def push conj)

(defn move-crates [amount src tgt crane-mode crates]
  (let [new-crates (->> src
                        crates
                        (take-last amount)
                        crane-mode)]
    (-> crates
        (update src #(vec (drop-last amount %)))
        (update tgt #(apply conj % new-crates)))))

(def inst-p
  (kc/bind [_ (lex/word "move")
            stack lex/dec-lit
            _ (lex/word "from")
            src lex/dec-lit
            _ (lex/word "to")
            tgt lex/dec-lit]
           (kc/return (partial move-crates stack
                         (dec src)
                         (dec tgt)))))

(def parse-instruction (partial kc/value inst-p))

(defn- parse-instructions [instructions]
  (->> instructions
       (map parse-instruction)))

(defn parse [[stacks instructions]]
  (vector (parse-stacks stacks)
          (parse-instructions instructions)))

(defn read-stacks [coll]
  (->> coll
       (partition-by empty?)
       ((juxt first last))
       parse))

(def default-crane reverse)

(defn find-top-stacks
  ([input] (find-top-stacks default-crane input))
  ([crane [stacks instructions]]
   (->> instructions
        (reduce #(%2 crane %1) stacks)
        (map top))))

(def find-top-stacks-9001 (partial find-top-stacks identity))

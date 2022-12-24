(ns aoc.2022.day10
  (:require [blancas.kern.core :as kc]
            [blancas.kern.lexer.basic :as lex]
            [clojure.string :as s]))

(defn parse-and-update-state
  "Takes a parser and a function that receives the result
  of the parser and the state returning a new state"
  [parser f]
  (kc/bind [result parser
            _ (kc/modify-state f result)
            state kc/get-state]
           (kc/return state)))

(def noop-pp (lex/token "noop"))


(def noop-p
  (parse-and-update-state
   noop-pp
   (fn [state _]
     (-> state
         (update :history conj (:x state))
         (update :cycle inc)))))

(def addx-pp
  (kc/>> (lex/token "addx") lex/dec-lit))

(def addx-p
  (parse-and-update-state
   addx-pp
   (fn [{:keys [x] :as state} x-inc]
     (-> state
         (update :history conj x (+ x x-inc))
         (update :x + x-inc)
         (update :cycle + 2)))))

(def instruction-p
  (kc/<|> addx-p noop-p))

(defn- parse-instruction [state instruction]
  (kc/value instruction-p instruction "no-file" state))

(defn collect-strengths [history]
  (->> 20
       (iterate (partial + 40))
       (take-while #(<= % 220))
       (map #(* % (history (- % 2))))
       (apply +)))

(def ->cycle-history
  (comp
   :history
   (partial reduce parse-instruction {:x 1 :cycle 1 :history []})))

(defn sum-signal-strengths [instructions]
  (->> instructions
       ->cycle-history
       collect-strengths))

(defn- intersects?
  [sprite-pos idx]
  (<= (dec sprite-pos) idx (inc sprite-pos)))

(defn- sprite->crt [sprite-pos cycle]
  (if (intersects? sprite-pos cycle) \# \.))

(defn crt-pixel [{:keys [sprite-pos] :as state} [cycle x-val]]
  (println "Cycle" cycle x-val sprite-pos (sprite->crt sprite-pos cycle))
  (-> state
      (update :crt conj (sprite->crt sprite-pos (mod cycle 40)))
      (assoc :sprite-pos x-val)))

(defn render-sprite [input]
  (->> input
       ->cycle-history
       (map-indexed vector)
       (reduce crt-pixel {:crt [] :sprite-pos 1})
       :crt
       (partition-all 40)
       (map s/join)
       ))

(ns aoc.2023.day4
  (:require [clojure.set :as cst]
            [blancas.kern.core :as kc :refer [many1]]
            [blancas.kern.lexer.basic :as lex :refer [dec-lit word token comma-sep1 semi-sep1]]))


(def p-card-title
  (kc/bind [_ (token "Card")
            id dec-lit
            _ lex/colon]
           (kc/return id)
           ))

(def p-number-list (many1 dec-lit))


(def p-card
  (kc/bind [id p-card-title
            winners p-number-list
            _ (lex/sym \|)
            numbers p-number-list]
           (kc/return [id {:winners (set winners) :numbers (set numbers) :copies 1}])))

(defn parse-card [card]
  (kc/value p-card card))

(defn- winner-count [{:keys [winners numbers]}]
  (->> winners
       (cst/intersection numbers)
       count))

(defn- winning-points [card]
  (->> card
       second
       winner-count
       (#(cond-> %
           (pos? %) (->> dec (Math/pow 2) int)))))

(defn cards-points [cards]
  (->> cards
       (map parse-card)
       (map winning-points)
       (apply +)))


(defn- increase-copies [pending idx {:keys [copies] :as card}]
  (let [total (winner-count card)]
    (->> (range (inc idx) (+ idx total 1))
         (reduce (fn [pending k] (update-in pending [k :copies] + copies)) pending))))


(defn- scratch-it [{:keys [idx pending] :as state}]
  (let [{:keys [copies] :as card} (pending idx)
        ]
    (-> state
        (update :total + copies)
        (update :idx inc)
        (update :pending dissoc idx)
        (update :pending increase-copies idx card))))

(defn total-scratch [cards]
  (->> cards
       (map parse-card)
       (into {})
       (assoc {} :total 0 :idx 1 :pending)
       (iterate scratch-it)
       (drop-while (comp seq :pending))
       first
       :total))

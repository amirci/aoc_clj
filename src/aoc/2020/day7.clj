(ns aoc.2020.day7
  (:require [clojure.edn :as edn]))

(defn ->bag [s]
  (when-let [match (re-matches #"(\d+) (.*) bags?\.?" s)]
    (->> match
         (drop 1)
         (map vector [:total :color])
         (into {})
         (#(update % :total edn/read-string)))))

(defn parse-rule [s]
  (let [[_ color rst] (re-matches #"(.*) bags? contain (.*)" s)]
    (->> (clojure.string/split rst #", ")
         (map ->bag)
         (filter seq)
         (vector color))))

(defn parse-rules [rules]
  (->> rules
       (map parse-rule)
       (into {})))

(defn can-contain? [color [_ bags]]
  (->> bags
       (map :color)
       set
       (#(% color))))

(defn bags-including [color rules]
  []
  (filter (partial can-contain? color) rules))


(defn eventually-step
  [{:keys [rules pending] :as state}]
  (let [fst (first pending)
        new-bags (bags-including fst rules)
        new-colors (map first new-bags)]
    (-> state
        (update :pending disj fst)
        (update :pending clojure.set/union (set new-colors))
        (update :found clojure.set/union (set new-bags)))))

(defn bags-eventually
  [color rules]
  (->> rules
       parse-rules
       (assoc {:pending #{color} :found #{}} :rules)
       (iterate eventually-step)
       (drop-while (comp seq :pending))
       first
       :found
       count))

(defn acc-bags [target rules]
  (->> target
       rules
       (map #(* (:total %) (acc-bags (:color %) rules)))
       (apply +)
       inc))

(defn bags-total [color rules]
  (->> rules
       parse-rules
       (acc-bags color)
       dec))

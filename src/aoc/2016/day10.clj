(ns aoc.2016.day10
  (:require [clojure.tools.trace :refer [trace]]
            [clojure.string :refer [join starts-with? split]]))


(def to-int #(Integer. %))

(defn find-by-val
  [v coll]
  (->> coll
       (filter (comp #(= % v) vec last))
       first))

(defn update-bot
  [bots bot v]
  (update bots bot #(-> (or % []) (conj (to-int v)) sort)))

(defmulti parse-tokens first)

(defmethod parse-tokens "value"
  [[_ v _ _ _ bot]]
   (fn [[bots output pending]]
       [(update-bot bots bot v) output pending]))

(defn update-target
  [[bots output pending] tgt l v]
  (if (= tgt "bot")
    [(update-bot bots l v) output pending]
    [bots (assoc output l v) pending]))

(defmethod parse-tokens "bot"
  [[_ bot _ _ _ ltgt lv _ _ _ htgt hv :as cmd]]
  (fn [[bots output pending :as state]]
    (let [[l h] (bots bot)]
      (if (or (nil? l) (nil? h))
        [bots output (conj pending (join " " cmd))]
        (-> state
            (update-target ltgt lv l)
            (update-target htgt hv h))))))

(defn parse-cmd
  [state cmd]
  (let [f (-> cmd (split #" ") parse-tokens)]
    (f state)))

(defn load-cmds
  [[bots output pending]]
  (reduce parse-cmd [bots output []] pending))

(defn load-cmds-loop
  [cmds]
  (->> [{} {} cmds]
       (iterate load-cmds)))

(defn find-bot
  [v cmds]
  (->> cmds
       load-cmds-loop
       (drop-while (fn [[bots _ pending]]
                     (and (not (empty? pending))
                          (not (find-by-val v bots)))))
       first
       first
       (find-by-val v)
       first))

(defn process-all
  [cmds]
  (->> cmds
       load-cmds-loop
       (drop-while (fn [[bots _ pending]]
                     (not (empty? pending))))
       first))

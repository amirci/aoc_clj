(ns aoc.2023.day2
  (:require [clojure.set :as cst]
            [blancas.kern.core :as kc :refer [<$> <*> >> skip-ws]]
            [blancas.kern.lexer.basic :as lex :refer [dec-lit word token comma-sep1 semi-sep1]]))

(def p-game-id
  (<$> second (<*> (token "Game") dec-lit lex/colon)))

(def p-boxes
  (<$> (fn [[n c]] {(keyword c) n})
          (<*> dec-lit
                  (token "red" "green" "blue"))))

(def p-sample
  (<$> #(apply merge %) (comma-sep1 p-boxes)))

(def p-sample-list
  (<$> (fn [samples] (apply merge-with max samples))
       (semi-sep1 p-sample)))


(def p-game
  (<*> p-game-id (skip-ws p-sample-list)))


(defn possible? [config [_ {:keys [red green blue]}]]
  (->> config
       (map vector [red green blue])
       (every? (partial apply <=)))
  )


(defn possible-games [config games]
  (->> games
       (map (partial kc/value p-game))
       (filter (partial possible? config))
       (map first)
       (apply +)))


(defn sum-of-powers [games]
  (->> games
       (map (partial kc/value p-game))
       (map #(->> %
                  second
                  vals
                  (apply *)))
       (apply +)))

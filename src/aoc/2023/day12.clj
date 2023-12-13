(ns aoc.2023.day12
  (:require [blancas.kern.core :as kc :refer [<*> <$>]]
            [blancas.kern.lexer.basic :as lex :refer [dec-lit comma-sep1]]))

(def p-springs-state
  (<$> (partial apply str)
       (kc/many1 (lex/one-of "?.#"))))

(def p-numbers
  (comma-sep1 dec-lit))

(def p-record
  (<*> p-springs-state p-numbers))

(defn parse-record [record]
  (kc/value p-record record))


(defn add-spaces-front-back [springs]
  (->> (format ".%s." springs)
       vec))


(defn parse-and-add-spaces [records]
  (->> records
       parse-record
       (#(update % 0 add-spaces-front-back))))

(defn restv [v] (subvec v 1))

(def possible-solutions
  (memoize
   (fn [springs specs last-group]
     (if (empty? springs)
       (if (and (empty? specs) (zero? last-group)) 1 0)
       (let [possible (if (= \? (springs 0)) [\. \#] [(springs 0)])]
         (reduce (fn [total-sols c]
                   (cond
                     (= \# c) (+ total-sols (possible-solutions (restv springs) specs (inc last-group)))
                     (pos? last-group) (cond-> total-sols
                                         (= last-group (get specs 0 0)) (+ (possible-solutions (restv springs) (restv specs) 0)))
                     :else (+ (possible-solutions (restv springs) specs 0))))
                 0
                 possible))))))

(defn possible-solutions* [[springs specs]]
  (possible-solutions springs specs 0))

(defn sum-possible-arrangements-parsed [records]
  (->> records
       (pmap possible-solutions*)
       (apply +)))

(defn sum-possible-arrangements [records]
  (->> records
       (map parse-and-add-spaces)
       sum-possible-arrangements-parsed))

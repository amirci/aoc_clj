(ns aoc.2023.day19
  (:require [blancas.kern.core :as kc :refer [<$> <*> >> <|> <:>]]
            [blancas.kern.lexer.basic :as lex :refer [dec-lit token sym braces comma-sep1 identifier]]))

(def rule-id-p identifier)

(defn terminus-state [k {:keys [part] :as state}]
  (-> state
      (update k conj part)
      (assoc :next nil)))

(def rejected-p
  (<$> (fn [_] (partial terminus-state :rejected)) (sym \R)))


(def accepted-p
  (<$> (fn [_] (partial terminus-state :accepted)) (sym \A)))


(defn set-next-id [rule-id state]
  (assoc state :next rule-id))


(def next-rule-id-p
  (<$> (fn [rule-id] (partial set-next-id rule-id))
       rule-id-p))


(def target-p
  (<|> rejected-p accepted-p next-rule-id-p))


(def compare-p
  (<$> {"<" < ">" > "=" = ">=" >= "<=" <=}
       (token "<" ">" "=")))


(def rating-p
  (<$> keyword (token "a" "m" "x" "s")))


(defn conditional-target
  [rating [comp-f nbr target-f] {:keys [part] :as state }]
  (let [value (part rating)]
    (assert value (format "The value for %s in %s is nil!" rating part))
    (when (comp-f value nbr)
      (target-f state))))


(def base-pred-target-p
  (<*> identifier compare-p dec-lit (>> lex/colon target-p)))


(def predicate-target-p
  (<$> (fn [[id & pred]]
         (partial conditional-target (keyword id) pred))
       base-pred-target-p))


(def predicate-list-p
  (<$> (fn [predicates]
         (fn [state] (->> predicates (keep #(% state)) first)))
       (braces
        (comma-sep1
         (<|> (<:> predicate-target-p)
              target-p)))))


(def rule-p (<*> rule-id-p predicate-list-p))


(defn gt-pred
  "s>nbr"
  [nbr low high]
  (cond
    (<= low nbr high) [[(inc nbr) high] [low nbr]]
    (< nbr low) [[low high]]
    (< high nbr) [nil [low high]]))

(defn lt-pred
  "s<nbr"
  [nbr low high]
  (cond
    (<= low nbr high) [[low (dec nbr)] [nbr high]]
    (< nbr low) [nil [low high]]
    (< high nbr) [[low high]]))

(defn eq-pred
  "s=nbr"
  [nbr low high]
  (cond
    (<= low nbr high) [[nbr nbr] [low (dec nbr)] [(inc nbr) high]]
    :else []))


(defn calculate-ranges [rating f-comp nbr set-target-f ratings]
  (let [[low high] (ratings rating)
        [match & rst] (f-comp nbr low high)]
    (cond-> []
      match (conj (-> ratings set-target-f (assoc rating match)))
      rst (concat (map (partial assoc ratings rating) rst)))))


(def comp->f
  {"<" lt-pred ">" gt-pred "=" eq-pred})


(defn eval-pending [[new-states old-pending] pred]
  (reduce (fn [[matching pending] rating]
            (let [[match & rst] (pred rating)]
              [(conj matching match)
               (concat pending rst)]))
          [new-states []]
          old-pending))

(defn eval-predicates [predicates state]
  (->> predicates
       (reduce eval-pending [[] [state]])
       first))


(defn set-pred-terminus [k c]
  (<$> (fn [_]
         (fn [state] (-> state (assoc k true :next nil) vector)))
       (sym c)))


(def target-pred-p
  (<|> (set-pred-terminus :rejected \R)
       (set-pred-terminus :accepted \A)
       (<$> (partial comp vector) next-rule-id-p)))


(def range-pred-target-p
  "Returns a function that given ratings returns
  different possibilities based on the comparison"
  (<$> (fn [[rating comp nbr target]]
         (partial calculate-ranges rating (comp->f comp) nbr target))
       (<*> rating-p (token "<" ">" "=") dec-lit
            (>> lex/colon (<$> (partial comp first) target-pred-p)))))


(def range-pred-list-p
  (<$> (fn [predicates] (partial eval-predicates predicates))
       (braces
        (comma-sep1
         (<|> (<:> range-pred-target-p) target-pred-p)))))


(def rule-2-p (<*> rule-id-p range-pred-list-p))



(defn parse-rule [parser rule]
  (let [parsed (kc/value parser rule)]
    (assert parsed (str "Can't parse " rule))
    parsed))


(defn parse-rules
  ([rules] (parse-rules rule-p rules))
  ([parser rules]
   (->> rules
        (map (partial parse-rule parser))
        (into {}))))


(def part-parser-p
  (<$> (partial into {})
       (braces
        (comma-sep1
         (<*> rating-p (>> (sym \=) dec-lit))))))


(def parse-part (partial kc/value part-parser-p))


(defn- apply-next-rule [rules {:keys [next] :as state}]
  (let [rule (rules next)]
    (assert rule (str "Couldn't find rule with id " next))
    (rule state)))


(defn- review-part [rules state part]
  (->> (assoc state :next "in" :part part)
       (iterate (partial apply-next-rule rules))
       (drop-while :next)
       first))


(defn sum-accepted-parts [[rules parts]]
  (let [rules (parse-rules rules)]
    (->> parts
         (map parse-part)
         (reduce (partial review-part rules)
                 {:accepted [] :rejected []})
         :accepted
         (mapcat vals)
         (apply +))))


(defn add-new-pending
  [state rules {rule-id :next :as ratings}]
  (->> ratings
       ((rules rule-id))
       (update state :pending concat)))


(defn follow-rule
  [rules {[fst & rst] :pending :as state}]
  (assert fst "There are no pending")
  (cond-> state
    :always (assoc :pending rst)
    (fst :accepted) (update :paths conj fst)
    (fst :next) (add-new-pending rules fst)))


(def default-ratings
  {:x [1 4000] :m [1 4000] :a [1 4000] :s [1 4000]})


(defn accepted-paths [rules]
  (->> {:paths []
        :pending [(assoc default-ratings :next "in")]}
       (iterate (partial follow-rule rules))
       (drop-while (comp seq :pending))
       first
       :paths))


(defn calc-combinations [ratings]
  (->> (select-keys ratings [:x :m :a :s])
       vals
       (map (fn [[l r]] (inc (- r l))))
       (apply *)))

(defn all-combinations [rules]
  (->> rules
       (parse-rules rule-2-p)
       accepted-paths
       (map calc-combinations)
       (apply +)))

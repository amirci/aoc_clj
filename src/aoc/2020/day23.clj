(ns aoc.2020.day23
  (:require [clojure.set :as set]))

(defn mk-map [digits]
  (let [cups (->> digits
                  cycle
                  (take (inc (count digits)) )
                  (partition 2 1)
                  (map #(zipmap [:cup :next] %))
                  (sort-by :cup)
                  vec)]
    {:run 0
     :idx (first digits)
     :cups cups
     :cmax (->> digits sort reverse (take 4) set)
     :all (set digits)}))

(defn found? [target n]
  (or (zero? n) (target n)))

(defn check-found [max n]
  (if (zero? n)
    max
    n))

(defn find-insert-cup [cup n3n all cmax]
  (let [target (disj (clojure.set/difference all n3n) cup)
        mm (apply max (clojure.set/difference cmax n3n))]
    (->> cup
         (iterate dec)
         (drop-while (complement (partial found? target)))
         first
         (check-found mm))))

(defn cut-paste [cups [a _ c d :as n4c] current smaller]
  (let [sc (cups (dec smaller))]
    (-> cups
        (assoc-in [(dec (:cup current)) :next] (:cup d))
        (assoc-in [(dec smaller) :next] (:cup a))
        (assoc-in [(dec (:cup c)) :next] (:next sc)))))

(defn next-cups [nbr cup cups]
  (->> cup
       (iterate #(cups (dec (:next %))))
       (drop 1)
       (take nbr)))

(defn move-cups [{:keys [cups idx cmax all run] :as state}]
  (when (zero? (mod run 1000000))
    (println "Run " run))

  (let [cup (cups (dec idx))
        n4c (next-cups 4 cup cups)
        n3n (map :cup (take 3 n4c))
        smaller (find-insert-cup (:cup cup) n3n all cmax)]
    (-> state
        (assoc :idx (:cup (last n4c)))
        (update :cups cut-paste n4c cup smaller)
        (update :run inc)
        (assoc :picked n3n))))

(defn ->digits [nbr]
  (map #(Character/digit % 10) nbr))

(defn ->cups [{:keys [cups idx]}]
  (->> (dec idx)
       cups
       (iterate #(cups (dec (:next %))))
       (take (count cups))
       (map :cup)
       (apply str)))


(defn play* [moves nbrs]
  (->> nbrs
       mk-map
       (iterate move-cups)
       (drop moves)
       first))

(defn play [moves nbr]
  (->> nbr
       ->digits
       (play* moves)
       (#(assoc % :idx 1))
       ->cups))


(defn ->million [cups]
  (->> cups
       (apply max)
       inc
       (#(range % 1000001))
       (concat cups)))


(defn play-mill [moves nbr]
  (->> nbr
       ->digits
       ->million
       (play* moves)
       :cups
       (#(next-cups 2 (% 0) %))
       (map :cup)
       (apply *)))



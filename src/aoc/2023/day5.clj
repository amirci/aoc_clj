(ns aoc.2023.day5
  (:require [blancas.kern.core :as kc :refer [many1 >> <$>]]
            [blancas.kern.lexer.basic :as lex :refer [dec-lit token]]))

(defn parse-range [numbers]
  (kc/value (<$> (fn [[a b c]] [[b (+ b c -1)] [a (+ a c -1)] c]) (many1 dec-lit)) numbers))

(defn parse-seeds [seeds]
  (kc/value (>> (token "seeds:") (many1 dec-lit)) seeds))


(defn mk-map [[map-title & ranges]]
  (let [[_ src tgt] (re-matches #"(\w+)-to-(\w+) map:" map-title)]
    (->> ranges
         (map parse-range)
         (sort-by first)
         (vector tgt)
         (vector src))))

(defn- in-range? [n [[a b] [c]]]
  (when (<= a n b)
    (let [diff (- n a)]
      (+ c diff))))

(defn- find-matching-range [n rngs]
  [n rngs]
  (->> rngs
       (keep (partial in-range? n))
       first
       (#(or % n))))

(defn- follow-k [almanac [k numbers]]
  (let [[tgt rngs] (almanac k)]
    (->> (for [n numbers] (find-matching-range n rngs))
         (vector tgt))))

(defn create-almanac [rst]
  (->> rst
       (partition-by empty?)
       (remove (comp empty? first))
       (map mk-map)
       (into {})))


(defn lowest-seed-location-single [[seeds _ & rst]]
  (let [numbers (parse-seeds seeds)
        almanac (create-almanac rst)]
    (->> ["seed" numbers]
         (iterate (partial follow-k almanac))
         (take 8)
         last
         second
         (apply min))))


(defn includes-range? [[c d] [[s1 s2] [t1]]]
  (when (<= s1 c d s2)
    (let [diff (- c s1)
          diff2 (- d c)]
      [(+ t1 diff) (+ t1 diff diff2)])))

(defn before? [[_ b] [[c]]]
  (< b c))

(defn overlap-ranges
  "Is not before and not after, so there are the output is r1, r2, r3 where:
  * r1: bit of the range before
  * r2: range that is included (potentially the whole range)
  * r3: bit after the range
  "
  [[a b] [[s1 s2] [t1 t2]]]
  (let [diff (- a s1)]
    (cond-> []
      (<= s1 a b s2) (conj [(+ t1 diff) (+ t1 diff (- b a))])
      (< s2 b) (conj [(+ t1 diff) t2] [(inc s2) b])
      (< a s1) (conj [a (dec s1)] [t1 (+ t1 (- b s1) )]))))

(defn- after? [[a] [[_ d]]]
  (< d a))

(defn find-matching-ranges [rng rngs2]
  (loop [matches [] rng rng [curr & rst] rngs2]
    (cond
      (nil? rng) matches
      (empty? curr) (concat matches [rng])
      (before? rng curr) (conj matches rng)
      (after? rng curr) (recur matches rng rst)
      :else (let [[overlap lefover] (overlap-ranges rng curr)]
              (recur (conj matches overlap) lefover rst)))))


(defn follow-ranges [almanac [k pending]]
  (let [[tgt rngs] (almanac k)]
    (->> pending
         (mapcat #(find-matching-ranges % rngs))
         (sort-by first)
         (vector tgt))))

(defn seed-location [almanac rng]
  (->> ["seed" [rng]]
       (iterate (partial follow-ranges almanac))
       (take 8)
       last
       second
       ffirst
       ))

(defn parse-seeds-as-ranges [seeds]
  (->> seeds
       parse-seeds
       (partition 2)
       (map (fn [[a b]] (vector a (+ a b -1) b)))))


(defn lowest-seed-location-from-ranges [[seeds _ & rst]]
  (let [ranges (parse-seeds-as-ranges seeds)
        almanac (create-almanac rst)]
    (->> ranges
         (map (partial seed-location almanac))
         (apply min)
         )))


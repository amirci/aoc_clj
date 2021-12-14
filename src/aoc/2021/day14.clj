(ns aoc.2021.day14
  (:require [clojure.set :as st]
            [clojure.tools.trace :as tr]))


(defn parse-rules
  [rules]
  (->> rules
       (map (partial re-find #"(\w{2}) -> (\w)"))
       (map rest)
       (map vec)
       (map #(-> %
                 (update 0 seq)
                 (update 1 first)))
       (into {})))


(defn parse
  [[tmpl _ & rules]]
  (let [r (parse-rules rules)
        m (->> (keys r) (map #(vector % 0)) (into {}))]
    (->> tmpl
         (partition 2 1)
         frequencies
         (hash-map :rules r
                   :endings (conj #{} (first tmpl) (last tmpl))
                   :zeros m
                   :template))))


(defn expand
  [templ rules zeros]
  (->> templ
       (filter (comp pos? second))
       (reduce
        (fn [m [[a b :as pair] n]]
          (let [l (rules pair)]
            (-> m
                (update [a l] + n)
                (update [l b] + n))))
        zeros)))


(defn poly-step
  [{:keys [zeros rules] :as state}]
  (-> state
      (update :template expand rules zeros)))


(def poly-seq (partial iterate poly-step))


(defn pairs->frequencies
  [m]
  (reduce
   (fn [m [[a b] n]]
     (-> m
         (update a #(+ n (or % 0)))
         (update b #(+ n (or % 0)))))
   {}
   m))


(defn de-double
  [endings m]
  (let [single? (= 1 (count endings))]
    (reduce-kv
     (fn [m k v]
       (let [ending? (endings k)
             v (cond
                 (and single? ending?) (+ 2 v)
                 (and (not single?) ending?) (inc v)
                 :else v)]
         (assoc m k (quot v 2))))
     {}
     m)))


(defn poly-steps
  [n lines]
  (let [{:keys [endings] :as parsed} (parse lines)]
    (->> parsed
         poly-seq
         (drop n)
         first
         :template
         pairs->frequencies
         (de-double endings))))


(defn subtract-poly
  [n lines]
  (->> lines
       (poly-steps n)
       (map second)
       (sort >)
       ((juxt first last))
       (apply -)))

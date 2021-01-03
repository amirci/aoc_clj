(ns aoc.2017.day15
  (:require
   [clojure.tools.trace :refer [trace]]
   [clojure.pprint :refer [cl-format]]))

(def factors {:a 16807 :b 48271})

(def divisor 2147483647)

(def lower (Math/pow 2 16))

(defn mk-gen [start factor]
  (->> start
       (iterate #(rem (* factor %) divisor))
       (drop 1)))

(defn mk-gen-picky [nbr start factor]
  (->> factor
       (mk-gen start)
       (filter #(zero? (mod % nbr)))))

(defn match-lower-16? [[a b :as pair]]
  (->> pair
       (map #(rem % lower))
       (apply =)))


(defn count-matching* [gen-a gen-b rounds]
  (->> gen-b
       (map vector gen-a)
       (take rounds)
       (filter match-lower-16?)
       count))


(defn count-matching [{:keys [a b]} rounds]
  (let [gen-a (mk-gen a (factors :a))
        gen-b (mk-gen b (factors :b))]
    (count-matching* gen-a gen-b rounds)))


(defn count-matching-picky [{:keys [a b]} rounds]
  (let [gen-a (mk-gen-picky 4 a (factors :a))
        gen-b (mk-gen-picky 8 b (factors :b))]
    (count-matching* gen-a gen-b rounds)))

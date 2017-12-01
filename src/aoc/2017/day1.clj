(ns aoc.2017.day1)

(defn to-digit [c] (- (int c) 48))

(defn captcha
  [[x & xs :as input]]
  (->> input
       seq
       (cons (last input))
       (partition-by identity)
       (filter #(> (count %) 1))
       (map (juxt (comp dec count)
                  (comp to-digit first)))
       (map #(apply * %))
       (reduce + 0)))

(defn add-half
  [s]
  (concat s (take (/ (count s) 2) s)))

(defn half-+-1 [s] (-> s count (/ 2) inc))

(defn matching?
  [[x & xs]]
  (= x (last xs)))

(defn captcha2
  [input]
  (->> input
       seq
       add-half
       (partition (half-+-1 input) 1)
       (filter matching?)
       (map (comp to-digit first))
       (reduce + 0)))

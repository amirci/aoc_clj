(ns aoc.2017.day1)

(defn to-digit [c] (- (int c) 48))

(defn captcha
  [input]
  (->> input
       (cons (last input))
       (partition 2 1)
       (filter #(apply = %))
       (map (comp to-digit first))
       (apply +)))

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
       add-half
       (partition (half-+-1 input) 1)
       (filter matching?)
       (map (comp to-digit first))
       (apply +)))

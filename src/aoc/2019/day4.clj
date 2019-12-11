(ns aoc.2019.day4)


(defn has-6-digits? [n] (<= 100000 n 999999))

(defn has-repeat-digits?
  [[a b c d e f]]
  (or (= a b)
      (= b c)
      (= c d)
      (= d e)
      (= e f)))

(defn ->digits [n] (map int (seq (str n))))

(defn never-decrease-digits?
  [s]
  (apply <= s))

(defn valid-pwd?
  [n]
  (let [s (->digits n)]
    (and
      (has-6-digits? n)
      (has-repeat-digits? s)
      (never-decrease-digits? s))))

(defn total-valid-pwd
  [rng]
  (->> rng
       (filter valid-pwd?)
       count))

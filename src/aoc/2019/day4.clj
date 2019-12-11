(ns aoc.2019.day4)


(defn has-6-digits? [n] (<= 100000 n 999999))

(defn has-repeat-digits?
  [[a b c d e f]]
  (or (= a b)
      (= b c)
      (= c d)
      (= d e)
      (= e f)))

(defn has-repeat-digits-2?
  [s]
  (->> s
       (partition-by identity)
       (filter #(= 2 (count %)))
       first))

(defn ->digits [n] (map int (seq (str n))))

(defn never-decrease-digits?
  [s]
  (apply <= s))

(defn valid-pwd?
  ([n] (valid-pwd? has-repeat-digits? n))
  ([repeat? n]
   (let [s (->digits n)]
     (and
       (has-6-digits? n)
       (repeat? s)
       (never-decrease-digits? s)))))

(defn total-valid-pwd
  [rng]
  (->> rng
       (filter valid-pwd?)
       count))

(defn total-valid-pwd-2
  [rng]
  (->> rng
       (filter (partial valid-pwd? has-repeat-digits-2?))
       count))

(ns aoc.2017.day4) 

(defn valid-passphrase?
  [ps]
  (->> ps
       frequencies
       vals
       (every? #(= 1 %))))

(defn valid-passphrase-anagram?
  [ps]
  (->> ps
       (map sort)
       valid-passphrase?))

(defn count-valid-passphrases
  ([ps] (count-valid-passphrases ps valid-passphrase?))
  ([ps f]
   (->> ps
        (map #(clojure.string/split % #" "))
        (filter f)
        count)))


(defn count-valid-passphrases-anagram
  [ps]
  (count-valid-passphrases ps valid-passphrase-anagram?))

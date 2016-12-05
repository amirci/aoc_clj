(ns aoc.2016.day5
  (:require [clojure.tools.trace :refer [trace]]
            [digest :refer :all]))


(def starts-with-5-zeros? #(clojure.string/starts-with? % "00000"))

(defn interesting-hashes
  [word index]
  (->> (iterate inc index)
       (map #(vector % (digest/md5 (str word %))))))

(defn pwd-char
  [word index]
  (->> (interesting-hashes word index)
       (drop-while (comp not starts-with-5-zeros? last))
       first))

(defn valid-index?
  [hsh]
  (and (starts-with-5-zeros? hsh)
       (-> hsh (nth 5) int (< 56))))

(defn valid-index-hash
  [word index]
  (->> (interesting-hashes word index)
       (drop-while (comp not valid-index? last))
       first))

(defn mk-pwd
  [word]
  (->> (iterate (fn [[i _]] (pwd-char word (inc i))) [-1])
       (take 9)
       (drop 1)
       (map #(nth (last %) 5))
       (apply str)))


(defn to-pwd
  [coll]
  (->> coll
       sort
       (map last)
       (apply str)))

(defn until-every-index-is-found
  [pwd [_ hsh]]
  (let [[pos c] (->> hsh (drop 5) (take 2))
        new-pwd (merge {pos c} pwd)]
    (if (= 8 (count new-pwd))
      (reduced new-pwd)
      new-pwd)))

(defn mk-pwd2
  [word]
  (->> (iterate (fn [[i _]] (valid-index-hash word (inc i))) [-1])
       (drop 1)
       (reduce until-every-index-is-found {})
       to-pwd))


(ns aoc.2016.day4
  (:require [clojure.tools.trace :refer [trace]]))


(defn comp-keys
  [[a b] [c d]]
  (if (= 0 (compare a c))
    (compare b d)
    (- (compare a c))))

(defn real-room?
  [[room-name _ checksum]]
  (->> room-name
       frequencies
       vec
       (map reverse)
       (sort-by identity comp-keys)
       (take 5)
       (map last)
       (apply str)
       (= checksum)))

(defn sum-sectors
  [rooms]
  (->> rooms
       (filter real-room?)
       (map #(Integer. (first (drop 1 %))))
       (reduce +)))

(defn rotate [l n] (concat (drop n l) (take n l)))

(def abc (apply str (map char (range 97 123))))

(defn decrypt-char
  [cipher c n]
  (let [i (clojure.string/index-of cipher c)]
    (if (= c \-) \  (char (+ 97 i)))))

(defn decrypt
  [[room-name sector]]
  (let [sector (Integer. sector)
        n (- 26 (mod sector 26))
        cipher (apply str (rotate abc n))]
    (->> room-name
         (map #(decrypt-char cipher % n))
         (apply str))))


(defn sector-for
  [room-name rooms]
  (->> rooms
       (filter #(= room-name (decrypt %)))
       first
       last))

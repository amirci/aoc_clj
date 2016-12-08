(ns aoc.2016.day7
  (:require [clojure.tools.trace :refer [trace]]))

(defn abba?
  [[a b c d]]
  (and (= b c) (= a d) (not= a b)))

(defn aba?
  [[a b c]]
  (and (= a c) (not= a b)))

(defn has-abba?
  [code]
  (->> code
       (partition 4 1)
       (filter abba?)
       first
       nil?
       not))

(defn tls?
  [code]
  (let [code (re-seq #"[a-z]+" code)
        supernet (take-nth 2 code)
        hypernet (take-nth 2 (rest code))]
    (and (some has-abba? supernet)
         (not-any? has-abba? hypernet))))


(defn to-bab [[a b]] [b a b])

(defn check-aba-bab
  [matches]
  (let [supernet (mapcat #(filter aba? %) (take-nth 2 matches))
        hypernet (mapcat #(filter aba? %) (take-nth 2 (rest matches)))]
    (->> supernet
         (map to-bab)
         set
         (clojure.set/intersection (set hypernet))
         empty?
         not)))

(defn ssl?
  [code]
  (->> code
       (re-seq #"[a-z]+")
       (map #(partition 3 1 %))
       check-aba-bab))

(defn count-tls
  [codes]
  (->> codes (filter tls?) count))

(defn count-ssl
  [codes]
  (->> codes (filter ssl?) count))


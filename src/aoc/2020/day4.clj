(ns aoc.2020.day4
  (:require [clojure.string :as s]
            [clojure.edn :as edn]))

;; byr (Birth Year) - four digits; at least 1920 and at most 2002.
;; iyr (Issue Year) - four digits; at least 2010 and at most 2020.
;; eyr (Expiration Year) - four digits; at least 2020 and at most 2030.
;; hgt (Height) - a number followed by either cm or in:
;; If cm, the number must be at least 150 and at most 193.
;; If in, the number must be at least 59 and at most 76.
;; hcl (Hair Color) - a # followed by exactly six characters 0-9 or a-f.
;; ecl (Eye Color) - exactly one of: amb blu brn gry grn hzl oth.
;; pid (Passport ID) - a nine-digit number, including leading zeroes.

(def eye-colors
  (-> "amb blu brn gry grn hzl oth"
      (s/split #" ")
      (->> (map keyword))
      set))


(defn update-nn [m k f & args]
  (if (m k)
    (apply update m k f args)
    m))

(defn re-matches* [s re] (re-matches re s))

(defn read-height [s]
  (->> s
       (re-matches #"(\d+)(\w{2})")
       (drop 1)))

(defn parse-fields [passport]
  (-> passport
      (update-nn :byr edn/read-string)
      (update-nn :iyr edn/read-string)
      (update-nn :eyr edn/read-string)
      (update-nn :hgt read-height)
      (update-nn :ecl (comp eye-colors keyword))
      (update-nn :hcl re-matches* #"#[\da-f]{6}")
      (update-nn :pid re-matches* #"\d{9}")))

(defn parse-line [s]
  (->> (s/split s #"\s")
       (map #(s/split % #":"))
       (into {})
       clojure.walk/keywordize-keys
       parse-fields))

(defn ->passport [lines]
  (->> lines
       (map parse-line)
       (apply merge)))

(defn parse-passports [lines]
  (->> lines
       (partition-by s/blank?)
       (remove (comp empty? first))
       (map ->passport)))

(defn valid? [passport]
  (let [ks (set (keys passport))
        len (count ks)]
    (or (= 8 len)
        (and (= 7 len) (nil? (ks :cid))))))

(defn count-valid [f lines]
  (->> lines
       parse-passports
       (filter f)
       count))

(def valid-passports (partial count-valid valid?))

(defn <! [& args]
  (and
   (not-any? nil? args)
   (apply < args)))

(defn valid-height? [[n s]]
  (and n
       (let [n (clojure.edn/read-string n)]
         (if (= "cm" s)
           (< 149 n 194)
           (< 58 n 77)))))

(defn valid-strict?
  [{:keys [byr iyr eyr hgt hcl ecl pid]}]
  (and
   (<! 1919 byr 2003)
   (<! 2009 iyr 2021)
   (<! 2019 eyr 2031)
   (valid-height? hgt)
   hcl
   ecl
   pid))


(def valid-strict-passports (partial count-valid valid-strict?))

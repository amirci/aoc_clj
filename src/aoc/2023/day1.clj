(ns aoc.2023.day1)


(def word-2-num
  {"one" 1
   "two" 2
   "three" 3
   "four" 4
   "five" 5
   "six" 6
   "seven" 7
   "eight" 8
   "nine" 9})

(defn parse-number [[a & rst :as s]]
  (if rst
    (word-2-num s)
    (- (int a) 48)))

(defn sum-digits [[d1 d2]] (+ (* d1 10) d2))

(defn sum-line [matches]
  (->> matches
       ((juxt first last))
       (map parse-number)
       sum-digits))

(defn sum-first-last [matches]
  (->> matches
       (map sum-line)
       (apply +)))

(def digits-or-words #"(?=(\d|one|two|three|four|five|six|seven|eight|nine))")

(defn match-digits-or-words
  [s]
  (->> s
       (re-seq digits-or-words)
       (map second)))

(defn calibration [lines]
  (->> lines
       (map (partial re-seq digits-or-words))
       (map (partial map second))
       sum-first-last))

(defn calibration-digits [lines]
  (->> lines
       (map (partial re-seq #"\d"))
       sum-first-last))


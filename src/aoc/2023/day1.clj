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

(defn parse-line [number-seq line]
  (->> line
       number-seq
       ((juxt first last))
       (map parse-number)
       sum-digits))

(def digits-or-words #"(?=(\d|one|two|three|four|five|six|seven|eight|nine))")

(defn match-digits-or-words
  [s]
  (->> s
       (re-seq digits-or-words)
       (map second)))

(defn calibration
  ([lines] (calibration match-digits-or-words lines))
  ([num-seq lines] (->> lines
                   (map (partial parse-line num-seq))
                   (apply +))))

(def calibration-digits (partial calibration #(re-seq #"\d" %)))


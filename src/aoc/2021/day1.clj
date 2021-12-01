(ns aoc.2021.day1)


(defn larger-than-previous
  [mms]
  (->> mms
       (partition 2 1)
       (filter (fn [[a b]] (< a b)))
       count))


(defn sum-windows
  [mms]
  (->> mms
       (partition 3 1)
       (map (partial apply +))))


(def larger-windows (comp larger-than-previous sum-windows))

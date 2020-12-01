(ns aoc.2020.day1)


(defn fix-report [entries]
  (first
   (for [x entries y entries :when (= 2020 (+ x y))]
     (* x y)) ))



(defn fix-report-3 [entries]
  (first
   (for [x entries y entries z entries
         :when (= 2020 (+ x y z))]
     (* x y z)) ))

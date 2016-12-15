(ns aoc.2016.day15-test
  (:require [clojure.test :refer :all]
            [aoc.2016.day15 :refer :all]))

;Disc #1 has 17 positions; at time=0, it is at position 5.
;Disc #2 has 19 positions; at time=0, it is at position 8.
;Disc #3 has 7 positions; at time=0, it is at position 1.
;Disc #4 has 13 positions; at time=0, it is at position 7.
;Disc #5 has 5 positions; at time=0, it is at position 1.
;Disc #6 has 3 positions; at time=0, it is at position 0.


(def discs [[17 5] [19 8] [7 1] [13 7] [5 1] [3 0]])

(def sample-discs [[5 4] [2 1]])

(deftest parta-sample-test
  (is (= 5 (find-time2 sample-discs))))

; rank 479
;(deftest part-a
;  (is (= 16824 (find-time2 discs))))


; find-time2 Elapsed time: 6045.588153 msecs
; find-time  Elapsed time: 6631.777064 msecs
;(deftest part-b
;  (is (= 3543984 (find-time2 (conj discs [11 0])))))


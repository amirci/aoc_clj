(ns aoc.2016.day11-test
  (:require [clojure.test :refer :all]
            [aoc.2016.day11 :refer :all]))


(def part-a-spec
  [1
   #{:gpm :mpm}
   #{:gco :gcu :gru :gpu}
   #{:mco :mcu :mru :mpu}
   #{}])

(def part-b-spec
  (let [[e f1 f2 f3 f4] part-a-spec]
    [e (conj f1 :gel :mel :gdi :mdi) f2 f3 f4]))

(def sample-spec
  [1
   #{:mh :ml}
   #{:gh}
   #{:gl}
   #{}])


(deftest possible-up-test
  (let [actual (possible-up sample-spec)
        expected [2 [:mli] [:gh :mh] [:gli] []]]
    (is (= [expected] actual))))

(deftest possible-moves-test
  (let [actual (possible-moves sample-spec)
        expected [2 [:mli] [:gh :mh] [:gli] []]]
    (is (= [expected] actual))))


(def middle-spec
  [3
   #{}
   #{:gcu :gpu :mpu}
   #{:gco :gpm :gru :mco :mpm}
   #{:mcu :mru}])

(deftest part-a
  (let [sp (time (shortest-top-floor part-a-spec))]
    ;(doseq [spec path] (print-spec spec))
    (is (= 33 sp))))

;(deftest part-b
;  (let [sp (time (shortest-top-floor part-b-spec))]
;    ;(doseq [spec path] (print-spec spec))
;    (is (= 57 sp))))


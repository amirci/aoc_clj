(ns aoc.2017.day22-test
  (:require [aoc.2017.day22 :as sut]
            [clojure.java.io :as io]
            [clojure.test :refer [deftest testing is]]))

(def input
  (->> "2017/day22.input.txt"
       io/resource
       slurp
       clojure.string/split-lines))


(def sample
  ["........."
   "........."
   "........."
   ".....#..."
   "...#....."
   "........."
   "........."
   "........."
   "........."])


(defn print-nodes [size {:keys [nodes pos dir]}]
  (println)
  (println)
  (print (apply str (repeat (* 2 (last pos) ) \space)) )
  (println ({[-1 0] "^" [1 0] "V" [0 -1] "<-" [0 1] "->"} dir ))
  (doseq [x (range size)]
    (doseq [y (range size)]
      (print (get nodes [x y] \.))
      (condp = pos
        [x y] (print \])
        [x (inc y)] (print \[)
        (print \space)))
    (println)))

(deftest part-a
  (testing "sample"
    (is (= 5587 (:infected (sut/bursts sample 10000)))))

  (testing "input"
    (is (= 5411 (:infected (sut/bursts input 10000))))))

(def tenm 10000000)

(deftest ^:slow part-b
  (testing "sample"
    (is (= 26 (:infected (sut/bursts-advanced sample 100))))
    (is (= 2511944 (:infected (sut/bursts-advanced sample tenm)))))

  (testing "input"
    (is (= 2511416 (:infected (sut/bursts-advanced input tenm))))))



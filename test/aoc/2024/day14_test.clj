(ns aoc.2024.day14-test
  (:require [aoc.2024.day14 :as sut]
            [clojure.string :as s]
            [clojure.test :as t]))

(defn- parse-robot [line]
  (->> line
       (re-seq #"(-?\d+)")
       (map second)
       (map #(Integer/parseInt %))
       (partition 2)
       (mapv vec)))


(def input
  (->> "resources/2024/day14.txt"
       slurp
       s/split-lines
       (mapv parse-robot)
       sut/multimap))



(def sample
  (->> ["p=0,4 v=3,-3"
        "p=6,3 v=-1,-3"
        "p=10,3 v=-1,2"
        "p=2,0 v=2,-1"
        "p=0,0 v=1,3"
        "p=3,0 v=-2,-2"
        "p=7,6 v=-1,-3"
        "p=3,0 v=-1,-2"
        "p=9,3 v=2,3"
        "p=7,3 v=-1,2"
        "p=2,4 v=2,-3"
        "p=9,5 v=-3,-3"]
       (map parse-robot)
       sut/multimap))


(def sample2
  (->> ["p=2,4 v=2,-3"]
       (map parse-robot)
       sut/multimap))


(t/deftest elapse-test
  (t/are [input expected] (= expected (dissoc (sut/elapse-second [7 11] input) :iteration))
    sample2 {[4 1] #{[2 -3]}}
    {[4 1] #{[2 -3]}} {[6 5] #{[2 -3]}}
    {[6 5] #{[2 -3]}} {[8 2] #{[2 -3]}}
    {[8 2] #{[2 -3]}} {[10 6] #{[2 -3]}}
    {[10 6] #{[2 -3]}} {[1 3] #{[2 -3]}}))


(def after-100
  (->> ["......2..1."
        "..........."
        "1.........."
        ".11........"
        ".....1....."
        "...12......"
        ".1....1...."]
       (mapv vec)))


(def after-100-robots
  (->> (for [row (range 7) col (range 11)
             :let [robot (get-in after-100 [row col])]
             :when (not= \. robot)]
         [[col row] (Integer/parseInt (str robot))])
       (into {})))


(t/deftest robots-iterations-test
  (t/is (= after-100-robots (sut/robots-after [7 11] 100 sample))))


(t/deftest safety-factor-after-test
  (t/is (= 12 (sut/safety-factor-after [7 11] 100 sample)))
  (t/is (= 211692000 (sut/safety-factor-after [103 101] 100 input))))


(t/deftest ^:slow iterations-until-tree-test
  (t/is (= 6587 (->> input
                      (sut/iterations-until-tree [103 101])
                      :iteration))))


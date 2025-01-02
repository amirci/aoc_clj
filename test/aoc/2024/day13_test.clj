(ns aoc.2024.day13-test
  (:require [aoc.2024.day13 :as sut]
            [clojure.string :as s]
            [clojure.test :as t]))

(def input
  (->> "resources/2024/day13.txt"
       slurp
       s/split-lines
       (partition-by empty?)
       (remove (comp empty? first))))


(def sample
  (->> ["Button A: X+94, Y+34"
        "Button B: X+22, Y+67"
        "Prize: X=8400, Y=5400"
        ""
        "Button A: X+26, Y+66"
        "Button B: X+67, Y+21"
        "Prize: X=12748, Y=12176"
        ""
        "Button A: X+17, Y+86"
        "Button B: X+84, Y+37"
        "Prize: X=7870, Y=6450"
        ""
        "Button A: X+69, Y+23"
        "Button B: X+27, Y+71"
        "Prize: X=18641, Y=10279"]
       (partition-by empty?)
       (remove (comp empty? first))))



(t/deftest cheapest-claw-test
  (t/is (= 280 (sut/cheapest-claw [[94 34] [22 67] [8400 5400]])))
  (t/is (nil? (sut/cheapest-claw [[26 66] [67 21] [12748 12176]])))
  (t/is (= 200 (sut/cheapest-claw [[17 86] [84 37] [7870 6450]])))
  (t/is (nil? (sut/cheapest-claw [[69 23] [27 71] [18641 10279]]))))

(t/deftest cheapest-claws-test
  (t/is (= 480 (sut/cheapest-claws sample)))
  (t/is (= 28887 (sut/cheapest-claws input))))

(def adjustment 10000000000000N)

(t/deftest cheapest-claws-adjust-test
  (t/is (= 875318608908 (sut/cheapest-claws adjustment sample)))
  (t/is (= 96979582619758 (sut/cheapest-claws adjustment input))))

(ns aoc.2022.day6-test
  (:require [aoc.2022.day6 :as sut]
            [clojure.string :as s]
            [clojure.test :refer [deftest is are] :as t]))

(def input
  (->> "resources/2022/day6.txt"
       slurp
       ))

(deftest part-1
  (are [s marker] (= marker (sut/find-marker s))
    "bvwbjplbgvbhsrlpgdmjqwftvncz" 5
    "nppdvjthqldpwncqszvftbrmjlhg" 6
    "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg" 10
    "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw" 11)
  (is (= 1816 (sut/find-marker input))))

(deftest part-2
  (are [s marker] (= marker (sut/find-message s))
    "mjqjpqmgbljsphdztnvjfqwrcgsmlb" 19
    "bvwbjplbgvbhsrlpgdmjqwftvncz" 23
    "nppdvjthqldpwncqszvftbrmjlhg" 23
    "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg" 29
    "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw" 26)
  (is (= 2625 (sut/find-message input))))



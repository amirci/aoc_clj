(ns aoc.2018.day9-test
  (:require [clojure.test :refer :all]
            [aoc.2018.day9 :as dut]))

(defn sample-game [] (dut/init-game 9 25))

(defn the-game [g] (select-keys g [:player :current :next-marble :marble-circle]))

(defn play-some-turns
  [n]
  (->> (sample-game)
       (iterate dut/play-turn)
       (drop n)
       first
       the-game))

;(0) -> sample-game

(deftest sample-players-test
  (testing "the first few rounds"
    (is (= [{:player 2 :current 1 :next-marble 2 :marble-circle [0 1]} ;[1]  0 (1)
            {:player 3 :current 1 :next-marble 3 :marble-circle [0 2 1]} ;[2]  0 (2) 1 
            {:player 4 :current 3 :next-marble 4 :marble-circle [0 2 1 3]} ;[3]  0  2  1 (3)
            {:player 5 :current 1 :next-marble 5 :marble-circle [0 4 2 1 3]} ;[4]  0 (4) 2  1  3 
            {:player 6 :current 3 :next-marble 6 :marble-circle [0 4 2 5 1 3]} ;[5]  0  4  2 (5) 1  3 
            {:player 7 :current 5 :next-marble 7 :marble-circle [0 4 2 5 1 6 3]} ;[6]  0  4  2  5  1 (6) 3 
            {:player 8 :current 7 :next-marble 8 :marble-circle [0 4 2 5 1 6 3 7]}   ;[7]  0  4  2  5  1  6  3 (7)
            {:player 9 :current 1 :next-marble 9 :marble-circle [0 8 4 2 5 1 6 3 7]} ;[8]  0 (8) 4  2  5  1  6  3  7 
            ]
           (->> (range 1 9) (map play-some-turns)))))

  (testing "Goes to player one after player 9"
    (is (= {:player 1 :current 3 :next-marble 10
            :marble-circle [0 8 4 9 2 5 1 6 3 7]} ;[9]  0  8  4 (9) 2  5  1  6  3  7 
           (play-some-turns 9))))

  (testing "Playing 22 marbles"
    (is (= {:player 5 :current 13 :next-marble 23
            :marble-circle [0 16 8 17 4 18 9 19 2 20 10 21 5 22 11 1 12 6 13 3 14 7 15]} 
           ; [4]  0 16  8 17  4 18  9 19  2 20 10 21  5(22)11  1 12  6 13  3 14  7 15
           (play-some-turns 22))))

  (testing "Playing 23 marbles"
    (is (= {:player 6 :current 6 :next-marble 24
            :marble-circle [0 16 8 17 4 18 19 2 20 10 21 5 22 11 1 12 6 13 3 14 7 15]} 
           ;[5]  0 16  8 17  4 18(19) 2 20 10 21  5 22 11  1 12  6 13  3 14  7 15
           (play-some-turns 23)))))


(deftest sample-games-test
  ;9 players; last marble is worth 25 points
  (is (= 32 (dut/part-a 9 25)))
  ;9 players; last marble is worth 48 points
  (is (= 63 (dut/part-a 9 48)))
  ;1 player; last marble is worth 48 points
  (is (= 95 (dut/part-a 1 48)))
  ;10 players; last marble is worth 1618 points: high score is 8317
  (is (= 8317 (dut/part-a 10 1618)))
  ;17 players; last marble is worth 1104 points: high score is 2764
  (is (= 2764 (dut/part-a 17 1104)))
  ;21 players; last marble is worth 6111 points: high score is 54718
  (is (= 54718 (dut/part-a 21 6111)))
  ;30 players; last marble is worth 5807 points: high score is 37305
  (is (= 37305 (dut/part-a 30 5807)))
  ;13 players; last marble is worth 7999 points: high score is 146373
  (is (= 146373 (dut/part-a 13 7999))))

(deftest part-a-test
  (is (= 398371 (dut/part-a 462 71938))))

;(deftest part-b-test
;  (is (= 3212830280 (dut/part-a 462 7193800))))


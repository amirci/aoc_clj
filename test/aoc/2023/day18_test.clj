(ns aoc.2023.day18-test
  (:require [aoc.2023.day18 :as sut]
            [clojure.string :as s]
            [clojure.test :refer [deftest is are] :as t]))

(def input
  (->> "resources/2023/day18.txt"
       slurp
       s/split-lines))

(def sample
  ["R 6 (#70c710)"
   "D 5 (#0dc571)"
   "L 2 (#5713f0)"
   "D 2 (#d2c081)"
   "R 2 (#59c680)"
   "D 2 (#411b91)"
   "L 5 (#8ceee2)"
   "U 2 (#caa173)"
   "L 1 (#1b58a2)"
   "U 2 (#caa171)"
   "R 2 (#7807d2)"
   "U 3 (#a77fa3)"
   "L 2 (#015232)"
   "U 2 (#7a21e3)" ])


(deftest parse-instr-test
  (is (= [[0 1] 6 (seq "70c710")]
         (sut/parse "R 6 (#70c710)"))))

(def sample-edges
  ["#######"
   "#.....#"
   "###...#"
   "..#...#"
   "..#...#"
   "###.###"
   "#...#.."
   "##..###"
   ".#....#"
   ".######"])


(deftest part1-test
  (is (= 62 (sut/lava-lake-area sample)))
  (is (= 42317 (sut/lava-lake-area input))))

(deftest parse-color-test
  (are [color exp-dir exp-len]
      (= [(sut/dir->dig exp-dir) exp-len]
         (sut/parse-color (str "D 0 (#" color ")")))
    "70c710" "R" 461937
    "0dc571" "D" 56407
    "5713f0" "R" 356671
    "d2c081" "D" 863240
    "59c680" "R" 367720
    "411b91" "D" 266681
    "8ceee2" "L" 577262
    "caa173" "U" 829975
    "1b58a2" "L" 112010
    "caa171" "D" 829975
    "7807d2" "L" 491645
    "a77fa3" "U" 686074
    "015232" "L" 5411
    "7a21e3" "U" 500254))



(deftest part2-test
  (is (= 952408144115  (sut/lava-lake-area-2 sample)))
  (is (= 83605563360288 (sut/lava-lake-area-2 input))))


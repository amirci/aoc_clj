(ns aoc.2020.day24-test
  (:require [aoc.2020.day24 :as sut]
            [clojure.java.io :as io]
            [clojure.test :as t :refer :all]))

(def input
  (->> "2020/day24.input.txt"
       io/resource
       slurp
       clojure.string/split-lines))


(def sample
  ["sesenwnenenewseeswwswswwnenewsewsw"
   "neeenesenwnwwswnenewnwwsewnenwseswesw"
   "seswneswswsenwwnwse"
   "nwnwneseeswswnenewneswwnewseswneseene"
   "swweswneswnenwsewnwneneseenw"
   "eesenwseswswnenwswnwnwsewwnwsene"
   "sewnenenenesenwsewnenwwwse"
   "wenwwweseeeweswwwnwwe"
   "wsweesenenewnwwnwsenewsenwwsesesenwne"
   "neeswseenwwswnwswswnw"
   "nenwswwsewswnenenewsenwsenwnesesenew"
   "enewnwewneswsewnwswenweswnenwsenwsw"
   "sweneswneswneneenwnewenewwneswswnese"
   "swwesenesewenwneswnwwneseswwne"
   "enesenwswwswneneswsenwnewswseenwsese"
   "wnwnesenesenenwwnenwsewesewsesesew"
   "nenewswnwewswnenesenwnesewesw"
   "eneswnwswnwsenenwnwnwwseeswneewsenese"
   "neswnwewnwnwseenwseesewsenwsweewe"
   "wseweeenwnesenwwwswnew"])

(deftest part-a
  (testing "sample"
    (is (= {:black 10 :white 5} (sut/flip-tiles sample))))

  (testing "input"
    (is (= {:black 377 :white 21} (sut/flip-tiles input)))))


(deftest part-b
  (is (= 2208 (sut/artsy 100 sample))))

(deftest ^:slow part-b-input
  (is (= 4231 (sut/artsy 100 input))))


(ns aoc.2018.day12-test
  (:require [aoc.2018.day12 :as sut]
            [clojure.java.io :as io]
            [clojure.test :refer [deftest testing is]]))

(defn parse [s]
  (-> s
      (clojure.string/split #" ")
      ((juxt (comp vec first) (comp first last)))))


(def input-rules
  (->> "2018/day12.input.txt"
       io/resource
       slurp
       clojure.string/split-lines
       (map parse)
       (into {})))


(def init-state
  (sut/->plant-set "#.#####.#.#.####.####.#.#...#.......##..##.#.#.#.###..#.....#.####..#.#######.#....####.#....##....#"))


(def sample-plants (sut/->plant-set "#..#.#..##......###...###"))


(def sample-rules
  (->> ["...## => #"
        "..#.. => #"
        ".#... => #"
        ".#.#. => #"
        ".#.## => #"
        ".##.. => #"
        ".#### => #"
        "#.#.# => #"
        "#.### => #"
        "##.#. => #"
        "##.## => #"
        "###.. => #"
        "###.# => #"
        "####. => #"]
       (map parse)
       (into {})))


(def expected-gens
  ["..#..#.#..##......###...###.........."
   "..#...#....#.....#..#..#..#.........."
   "..##..##...##....#..#..#..##........."
   ".#.#...#..#.#....#..#..#...#........."
   "..#.#..#...#.#...#..#..##..##........"
   "...#...##...#.#..#..#...#...#........"
   "...##.#.#....#...#..##..##..##......."
   "..#..###.#...##..#...#...#...#......."
   "..#....##.#.#.#..##..##..##..##......"
   "..##..#..#####....#...#...#...#......"
   ".#.#..#...#.##....##..##..##..##....."
   "..#...##...#.#...#.#...#...#...#....."
   "..##.#.#....#.#...#.#..##..##..##...."
   ".#..###.#....#.#...#....#...#...#...."
   ".#....##.#....#.#..##...##..##..##..."
   ".##..#..#.#....#....#..#.#...#...#..."
   "#.#..#...#.#...##...#...#.#..##..##.."
   ".#...##...#.#.#.#...##...#....#...#.."
   ".##.#.#....#####.#.#.#...##...##..##."
   "#..###.#..#.#.#######.#.#.#..#.#...#."
   "#....##....#####...#######....#.#..##"])

(defn min-max [gens]
  (->> gens
       (map (juxt first last))
       (apply map vector)
       ((fn [[mns mxs]] [(apply min mns) (apply max mxs)]))))


(defn ->plant-row [mn mx plants]
  (->> (range mn (inc mx))
       (map #(if (plants %) \# \.))
       (apply str)))


(defn ->plants [gens]
  (let [[mn mx] (min-max gens)]
    (->> gens
         (map (partial ->plant-row mn mx)))))


(deftest part-a
  (testing "plant rule"
    (is (= 0 (sut/plant-rule sample-rules sample-plants 0)))
    (is (= nil (sut/plant-rule sample-rules sample-plants 1)))
    (is (= 4 (sut/plant-rule sample-rules sample-plants 4))))

  (testing "generations"
    (is (= expected-gens
           (->> (range 0 21)
                (map (partial sut/generations sample-rules sample-plants))
                ->plants))))

  (testing "sum last generation"
    (is (= 325
           (apply + (sut/generations sample-rules sample-plants 20))))
    (is (= 3494
           (apply + (sut/generations input-rules init-state 20))))))


(def fiftyb 50000000000)

(deftest part-b
  (testing "long gen"
    (is (= 2850000002454 (sut/long-gen input-rules init-state fiftyb)))))


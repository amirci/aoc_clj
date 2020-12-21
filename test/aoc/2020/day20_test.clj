(ns aoc.2020.day20-test
  (:require [aoc.2020.day20 :as sut]
            [clojure.java.io :as io]
            [clojure.test :as t :refer :all]))

(defn ->tile [lines]
  [(->> lines
            first
            (re-matches #"Tile (\d+):")
            last
            read-string)
   (->> lines (drop 1) (take 10))])

(def input
  (->> "2020/day20.input.txt"
       io/resource
       slurp
       clojure.string/split-lines
       (partition-all 12)
       (map ->tile)
       (into {})))


(def sample
  (->> "2020/day20.sample.txt"
       io/resource
       slurp
       clojure.string/split-lines
       (partition-all 12)
       (map ->tile)
       (into {})))


(defn print-tile [tile]
  (when (:pos tile)
    (println "\n\nPOS" (:pos tile)))
  (let [tile (or (:tile tile) tile)]
    (->> tile
         (map #(apply str %))
         (clojure.string/join "\n")
         (str "\n")
         println))
  tile)

(deftest part-a
  (testing "Sides nbrs"
    (is (= [587 710 318 564] (map sut/ch->bin ["#..#..#.##"
                                               "#.##...##."
                                               ".#..#####."
                                               "#...##.#.."])))

    (is (= [587 710 318 564] (sut/side-nbrs
                              ["#..#..#.##"
                               "..####...."
                               ".#####..##"
                               "..#.#...##"
                               "##.#.##.#."
                               "#..##.###."
                               "....#.#..."
                               "##.##.#..#"
                               "..###.##.#"
                               ".#..#####."]))))


  (testing "sample"
    (is (= 20899048083289 (sut/find-corners sample))))

  (testing "input"
    (is (= 15670959891893 (sut/find-corners input)))))


(def tiles (sut/add-common (sut/add-sides sample)))

(def found (:found (sut/init-state sample 1951) ))


(deftest part-b
  (testing "Top Left corner"
    (is (= [587 710 318 564] (-> 1951
                                 tiles
                                 sut/tl-corner
                                 :tile
                                 sut/side-nbrs))))

  (let []
    (testing "Match tile step"
      (is (= [318 210 616 231]
             (-> (sut/find-orientation
                  (select-keys found [1951])
                  (tiles 2311)
                  tiles)
                 (get-in [2311 :sides]))))
      (is (= [962 85 9 710]
             (-> (sut/find-orientation
                  (select-keys found [1951])
                  (tiles 2729)
                  tiles)
                 (get-in [2729 :sides])))))   )


  (testing "sample"
      (is (= 273 (sut/water-roughness sample))))

  (testing "input"
      (is (= 1964 (sut/water-roughness input)))))



(ns aoc.2019.day10-test
  (:require [aoc.2019.day10 :as sut]
            [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.test :as t :refer [deftest is testing]]))

(def asteroid? (partial not= \.))

(defn mk-line-map
  [roids [row line]]
  (reduce
   (fn [roids [col c]]
     (if (asteroid? c)
       (conj roids [col row])
       roids))
   roids
   (map-indexed vector line)))

(defn mk-asteroid-map
  [lines]
  (->> lines
       (map-indexed vector)
       (reduce mk-line-map #{})))

(def asteroids
  (->> "2019/day10.input.txt"
       io/resource
       slurp
       clojure.string/split-lines
       mk-asteroid-map))

(def sample-1
  (mk-asteroid-map [".#..#"
                    "....."
                    "#####"
                    "....#"
                    "...##"]))


(def sample-2
  (mk-asteroid-map ["......#.#."
                    "#..#.#...."
                    "..#######."
                    ".#.#.###.."
                    ".#..#....."
                    "..#....#.#"
                    "#..#....#."
                    ".##.#..###"
                    "##...#..#."
                    ".#....####"]))

(def sample-1a
  (mk-asteroid-map ["#........." ; 0
                    "...A......" ; 1
                    "...B..a..." ; 2
                    ".EDCG....a" ; 3
                    "..F.c.b..." ; 4
                    ".....c...." ; 5
                    "..efd.c.gb" ; 6
                    ".......c.." ; 7
                    "....f...c." ; 8
                    "...e..d..c"]))

(deftest in-sight-test
  (is (= (into
           {}
           (for [p [[3 1] [3 2] [3 3] [2 3] [1 3] [2 4] [4 3]]]
             [(sut/angle-360 [0 0] [0 -1] p) p])) 
         (sut/in-sight [0 0] sample-1a))))


(def sample-3
  (mk-asteroid-map ["#.#...#.#."
                    ".###....#."
                    ".#....#..."
                    "##.#.#.#.#"
                    "....#.#.#."
                    ".##..###.#"
                    "..#...##.."
                    "..##....##"
                    "......#..."
                    ".####.###."]))

(def sample-4
  (mk-asteroid-map [".#..#..###"
                    "####.###.#"
                    "....###.#."
                    "..###.##.#"
                    "##.##.#.#."
                    "....###..#"
                    "..#.#..#.#"
                    "#..#.#.###"
                    ".##...##.#"
                    ".....#.#.."]))

(def sample-5
  (mk-asteroid-map [".#..##.###...#######"
                    "##.############..##."
                    ".#.######.########.#"
                    ".###.#######.####.#."
                    "#####.##.#.##.###.##"
                    "..#####..#.#########"
                    "####################"
                    "#.####....###.#.#.##"
                    "##.#################"
                    "#####.##.###..####.."
                    "..######..##.#######"
                    "####.##.####...##..#"
                    ".#####..#.######.###"
                    "##...#.##########..."
                    "#.##########.#######"
                    ".####.#.###.###.#.##"
                    "....##.##.###..#####"
                    ".#.#.###########.###"
                    "#.#.#.#####.####.###"
                    "###.##.####.##.#..##"]))

(def samples
  {[[3 4] 8] sample-1
   [[5 8] 33] sample-2
   [[1 2] 35] sample-3
   [[6 3] 41] sample-4
   [[11 13] 210] sample-5})

(deftest part-a-samples-test
  (doseq [[expected sample] samples]
    (is (= expected
           (sut/best-base-location sample)))))

(deftest part-a-test
  (is (= [[29 28] 256]
         (sut/best-base-location asteroids))))

(def sample-b-1
  (mk-asteroid-map [".#....###BD...#.."
                    "##...##.AC#FG..I#"
                    "##...#...E.HJKLM."
                    "..#.....X...N##.."
                    "..#.R.....Q....PO"]))


(deftest vaporizing-test
  (is (= [[8 1] [9 0] [9 1] [10 0] [9 2] [11 1] [12 1] [11 2] [15 1]]
         (take 9 (sut/vaporize-all [8 3] sample-b-1 ))))
  (is (= [[12 2] [13 2] [14 2] [15 2] [12 3] [16 4] [15 4] [10 4] [4 4]]
         (take 9 (drop 9 (sut/vaporize-all [8 3] sample-b-1 )))))
  (is (= [[2 4] [2 3] [0 2] [1 2] [0 1] [1 1] [5 2] [1 0] [5 1]]
         (take 9 (drop 18 (sut/vaporize-all [8 3] sample-b-1 )))))
  (is (= [[6 1] [6 0] [7 0] [8 0] [10 1] [14 0] [16 1] [13 3] [14 3]]
         (take 9 (drop 27 (sut/vaporize-all [8 3] sample-b-1 ))))))

(deftest vaporizing-sample-5-test
  (doseq [[idx expected] {1 [11 12] 2 [12 1] 3 [12 2]
                          10 [12 8] 20 [16 0] 50 [16 9]
                          100 [10 16] 199 [9 6]
                          200 [8 2] 201 [10 9] 299 [11 1]}]
    (testing (str "nth-vaporize-" idx "-should-be-" expected)
      (is (= expected
             (first (drop (dec idx) (sut/vaporize-all  [11 13] sample-5))))))))

(deftest part-b-test
  (is (= [17 7]
         (first (drop 199 (sut/vaporize-all [29 28] asteroids))))))


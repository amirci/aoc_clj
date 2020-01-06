(ns aoc.2019.day10-test
  (:require [aoc.2019.day10 :as sut]
            [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.test :as t :refer [deftest is]]))

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

(comment
(sort (mapcat identity (vals (sut/in-sight [5 8] sample-2)))  )
(sut/in-sight [5 8] sample-2)

(sut/collinear? [5 8] [8 8] [1 8])
(sut/slope [5 8] [8 9])
(sut/slope [5 8] [2 7])

{0.0 #{[8 8] [1 8]}, ; ok
 2.0 #{[2 2]},
 -2.0 #{[8 2]},
 2.5 #{[3 3]},
 -2.5 #{[7 3]},
 3.0 #{[3 2]},
 -3.0 #{[7 2]},
 3.5 #{[3 1]},
 4.0 #{[4 4]},
 0.25 #{[9 9] [1 7]},
 -0.25 #{[1 9] [9 7]},
 -5.0 #{[6 3]},
 6.0 #{[4 2]},
 -6.0 #{[6 2]},
 -8.0 #{[6 0]},
 0.5 #{[7 9]},
 -0.5 #{[7 7]},
 -0.75 #{[9 5]},
 1.0 #{[4 7] [6 9]},
 1.25 #{[1 3]},
 -1.5 #{[7 5]},
 :infinity #{[5 3]},
 -2.6666667 #{[8 0]},
 0.33333334 #{[8 9] [2 7]},
 -0.33333334 #{[8 7]},
 -0.6666667 #{[8 6]},
 0.4 #{[0 6]},
 1.4 #{[0 1]}}
)

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
  (is (= {(float 0.33333334) #{[9 3] [3 1] [6 2]} ; A
          (float 0.6666667) #{[9 6] [6 4] [3 2]} ; B
          1.0 #{[8 8] [7 7] [3 3] [6 6] [9 9] [5 5] [4 4]} ; C
          1.5 #{[2 3] [4 6] [6 9]} ; D
          3.0 #{[3 9] [1 3] [2 6]} ; E
          2.0 #{[4 8] [2 4] [3 6]} ; F
          0.75 #{[4 3] [8 6]}} ; G
         (->> (disj sample-1a [0 0])
              (group-by (partial sut/slope [0 0]))
              (reduce-kv #(assoc %1 %2 (set %3)) {}))))

  (is (= (into
           {}
           (for [p [[3 1] [3 2] [3 3] [2 3] [1 3] [2 4] [4 3]]]
             [(sut/slope [0 0] p) #{p}])) 
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

(->> (disj sample-3 [1 2])
     (group-by (partial sut/slope [1 2]))
     (reduce-kv #(assoc %1 %2 (set %3)) {}))

(let [ins (sut/in-sight [1 2] sample-3)
      all (reduce-kv #(clojure.set/union %1 %3) #{} ins)
      ]
  (count all)
  #_(clojure.set/difference sample-3 all))

(let [ins (sut/in-sight [1 2] sample-3)
      all (reduce-kv #(clojure.set/union %1 %3) #{} ins)
      ]
  (count all)
  #_(clojure.set/difference sample-3 all))

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

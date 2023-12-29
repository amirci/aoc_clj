(ns aoc.2023.day16-test
  (:require [aoc.2023.day16 :as sut]
            [clojure.string :as s]
            [clojure.test :refer [deftest is testing are] :as t]))

(def input
  (->> "resources/2023/day16.txt"
       slurp
       s/split-lines))

(def sample
  [".|...\\...."
   "|.-.\\....."
   ".....|-..."
   "........|."
   ".........."
   ".........\\"
   "..../.\\\\.."
   ".-.-/..|.."
   ".|....-|.\\"
   "..//.|...."])


(def tile-map (sut/load-tile-map sample))

(deftest split-dash-test
  (let [pt [7 4] sd (sut/split-dash pt)
        bl (sut/mk-beam [7 3] :l)
        br (sut/mk-beam [7 5] :r)]
    (are [dir expected] (= expected (sd (sut/mk-beam pt dir)))
      :d [:split bl br]
      :u [:split bl br]
      :l bl
      :r br)))


(deftest split-pipe-test
  (let [pt [7 4] sp (sut/split-pipe pt)
        bu (sut/mk-beam [6 4] :u)
        bd (sut/mk-beam [8 4] :d)]
    (are [dir xp] (= xp (sp (sut/mk-beam pt dir)))
      :r [:split bu bd]
      :l [:split bu bd]
      :u bu
      :d bd)))


(deftest mirror-forward-test
  (let [pt [7 4] sp (sut/reflect-90-forward pt)]
    (are [dir xp] (= xp (sp (sut/mk-beam pt dir)))
      :r (sut/mk-beam [6 4] :u)
      :l (sut/mk-beam [8 4] :d)
      :u (sut/mk-beam [7 5] :r)
      :d (sut/mk-beam [7 3] :l))))


(deftest mirror-backward-test
  (let [pt [7 4] sp (sut/reflect-90-backward pt)]
    (are [dir xp] (= xp (sp (sut/mk-beam pt dir)))
      :r (sut/mk-beam [8 4] :d)
      :l (sut/mk-beam [6 4] :u)
      :u (sut/mk-beam [7 3] :l)
      :d (sut/mk-beam [7 5] :r))))


(deftest follow-beam-test
  (testing "mirror forward"
    (let [beam (sut/mk-beam [7 4] :r)]
      (is (= [(sut/mk-beam [6 4] :u) [] #{beam}]
             (sut/follow-beam tile-map [beam [] #{}]))))))

(def expected-energized
  ["######...."
   ".#...#...."
   ".#...#####"
   ".#...##..."
   ".#...##..."
   ".#...##..."
   ".#..####.."
   "########.."
   ".#######.."
   ".#...#.#.."])

(defn highlight-energized [tiles visited]
  (->> (for [x (range (count tiles))
             y (range (count (first tiles)))]
         [x y])
       (reduce (fn [tiles pt]
                 (assoc-in tiles pt
                           (if (visited pt) \# \.)))
               (mapv vec tiles))
       (map #(apply str %))))

(deftest energize-test
  (is (= expected-energized
         (->> sample
              sut/load-tile-map
              (sut/energize (sut/mk-beam [0 0] :r))
              (map first)
              set
              (highlight-energized sample)))))

(deftest part1-test
  (is (= 46 (sut/count-energized-tiles sample)))
  (is (= 7979 (sut/count-energized-tiles input))))

(deftest part2-test
  (is (= 51 (sut/max-energized-tiles sample)))
  (is (= 8437 (sut/max-energized-tiles input))))

(ns aoc.2024.day16-test
  (:require [aoc.2024.day16 :as sut]
            [clojure.string :as s]
            [clojure.test :as t]))


(defn parse-grid [lines]
  (let [grid (mapv vec lines)
        rows (count lines)
        cols (count (first lines))]
    (->> (for [row (range rows) col (range cols)] [row col])
         (reduce (fn [m [row col]]
                     (condp = (get-in grid [row col])
                       \# (update m :walls conj [row col])
                       \S (assoc m :start [row col])
                       \E (assoc m :end [row col])
                       m))
                 {:rows rows :cols cols :walls #{}}))))


(def input
  (->> "resources/2024/day16.txt"
                slurp
                s/split-lines
                parse-grid))

(def s0
  (->> ["###############"
        "#.......#....E#"
        "#.#.###.#.###.#"
        "#.....#.#...#.#"
        "#.###.#####.#.#"
        "#.#.#.......#.#"
        "#.#.#####.###.#"
        "#...........#.#"
        "###.#.#####.#.#"
        "#...#.....#.#.#"
        "#.#.#.###.#.#.#"
        "#.....#...#.#.#"
        "#.###.#.#.#.#.#"
        "#S..#.....#...#"
        "###############"]
       (mapv vec)))

(def sample-0
  (parse-grid s0))


(def sample
  (->> ["#################"
        "#...#...#...#..E#"
        "#.#.#.#.#.#.#.#.#"
        "#.#.#.#...#...#.#"
        "#.#.#.#.###.#.#.#"
        "#...#.#.#.....#.#"
        "#.#.#.#.#.#####.#"
        "#.#...#.#.#.....#"
        "#.#.#####.#.###.#"
        "#.#.#.......#...#"
        "#.#.###.#####.###"
        "#.#.#...#.....#.#"
        "#.#.#.#####.###.#"
        "#.#.#.........#.#"
        "#.#.#.#########.#"
        "#S#.............#"
        "#################"]
       parse-grid))


(t/deftest part-1-2
  (t/is (= [7036 45] (sut/cheapest-path sample-0)))
  (t/is (= [11048 64] (sut/cheapest-path sample)))
  (t/is (= [90440 479] (sut/cheapest-path input))))

#_(time
 (sut/cheapest-path input)) ;; 2725 milliseconds

(defn grid->chars [paths? {:keys [rows cols start end walls]}]
  (->> (for [row (range rows)]
         (for [col (range cols)]
           (cond
             (walls [row col]) \#
             (= start [row col]) \S
             (= end [row col]) \E
             (paths? [row col]) \O
             :else \.)))
       (map #(apply str %))
       (s/join "\n")))

(defn print-all-paths [{:keys [end] :as input}]
  (let [[_ paths] (sut/cheapest-path input)]
    (->> paths
         (sort-by first)
         (grid->chars paths)
         (println (count paths) (paths end) "\n"))))

#_(->> ["###############"
      "#.......#....O#"
      "#.#.###.#.###O#"
      "#.....#.#...#O#"
      "#.###.#####.#O#"
      "#.#.#.......#O#"
      "#.#.#####.###O#"
      "#..OOOOOOOOO#O#"
      "###O#O#####O#O#"
      "#OOO#O....#O#O#"
      "#O#O#O###.#O#O#"
      "#OOOOO#...#O#O#"
      "#O###.#.#.#O#O#"
      "#O..#.....#OOO#"
      "###############"]
     (s/join "\n")
     println)

#_(print-all-paths sample-0)

#_(->> ["#################"
      "#...#...#...#..O#"
      "#.#.#.#.#.#.#.#O#"
      "#.#.#.#...#...#O#"
      "#.#.#.#.###.#.#O#"
      "#OOO#.#.#.....#O#"
      "#O#O#.#.#.#####O#"
      "#O#O..#.#.#OOOOO#"
      "#O#O#####.#O###O#"
      "#O#O#..OOOOO#OOO#"
      "#O#O###O#####O###"
      "#O#O#OOO#..OOO#.#"
      "#O#O#O#####O###.#"
      "#O#O#OOOOOOO..#.#"
      "#O#O#O#########.#"
      "#O#OOO..........#"
      "#################"]
     (s/join "\n")
     println)

#_(print-all-paths sample)

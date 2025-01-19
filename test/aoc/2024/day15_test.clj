(ns aoc.2024.day15-test
  (:require [aoc.2024.day15 :as sut]
            [clojure.string :as s]
            [clojure.test :as t]
            [quil.core :as q]
            [quil.middleware :as m]))

(defn parse-grid [lines]
  (let [grid (mapv vec lines)
        rows (count lines)
        cols (count (first lines))]
    (->> (for [row (range rows) col (range cols)
               :let [char (get-in grid [row col])]
               :when (not= \. char)]
           (condp = char
             \# [[row col] :wall]
             \O [[row col] :box]
             \[ [[row col] :box-l]
             \] [[row col] :box-r]
             \@ [:robot [row col]]
             (assert false (str "Unknown char: " char "at" [row col]))))
         (into {:rows rows :cols cols}))))

(defn- parse-double-grid [lines]
  (-> lines
      parse-grid
      (assoc :push-boxes sut/push-double-boxes)))

(defn parse-instructions [lines]
  (let [[grid rest] (split-with seq lines)
        moves (->> rest (drop 1) (s/join ""))]
    (vector moves (parse-grid grid))))


(def input
  (->> "resources/2024/day15.txt"
       slurp
       s/split-lines
       parse-instructions))


(def sample
  (->> ["##########"
        "#..O..O.O#"
        "#......O.#"
        "#.OO..O.O#"
        "#..O@..O.#"
        "#O#..O...#"
        "#O..O..O.#"
        "#.OO.O.OO#"
        "#....O...#"
        "##########"
        ""
        "<vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^"
        "vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v"
        "><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<"
        "<<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^"
        "^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><"
        "^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^"
        ">^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^"
        "<><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>"
        "^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>"
        "v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^"]
       parse-instructions))


(def small-sample
  (->> ["########"
        "#..O.O.#"
        "##@.O..#"
        "#...O..#"
        "#.#.O..#"
        "#...O..#"
        "#......#"
        "########"
        ""
        "<^^>>>vv<v>>v<<"]
       parse-instructions))

(defn- grid->chars [{:keys [rows cols move] :as grid}]
  (for [row (range rows)]
    (apply str
           (for [col (range cols) :let [char (grid [row col])]]
             (cond
               (= [row col] (:robot grid)) \@
               (= char :wall) \#
               (= char :box) \O
               (= char :box-l) \[
               (= char :box-r) \]
               :else \.)))))


(defn- grid->chars* [{:keys [rows cols move] :as grid}]
  (concat
   [(str "Move " move)]
   (grid->chars grid)))


(t/deftest move-robot-test
  (->> "<^^>>>v"
       (map sut/move->dir)
       (reductions sut/move-robot-in-dir (second small-sample))
       (drop 1)
       (map grid->chars)
       (= [["########"
            "#..O.O.#"
            "##@.O..#"
            "#...O..#"
            "#.#.O..#"
            "#...O..#"
            "#......#"
            "########"]
           ["########"
            "#.@O.O.#"
            "##..O..#"
            "#...O..#"
            "#.#.O..#"
            "#...O..#"
            "#......#"
            "########"]
           ["########"
            "#.@O.O.#"
            "##..O..#"
            "#...O..#"
            "#.#.O..#"
            "#...O..#"
            "#......#"
            "########"]
           ["########"
            "#..@OO.#"
            "##..O..#"
            "#...O..#"
            "#.#.O..#"
            "#...O..#"
            "#......#"
            "########"]
           ["########"
            "#...@OO#"
            "##..O..#"
            "#...O..#"
            "#.#.O..#"
            "#...O..#"
            "#......#"
            "########"]
           ["########"
            "#...@OO#"
            "##..O..#"
            "#...O..#"
            "#.#.O..#"
            "#...O..#"
            "#......#"
            "########"]
           ["########"
            "#....OO#"
            "##..@..#"
            "#...O..#"
            "#.#.O..#"
            "#...O..#"
            "#...O..#"
            "########"]])
       t/is))


(t/deftest follow-path-test
  (t/is (= ["##########"
            "#.O.O.OOO#"
            "#........#"
            "#OO......#"
            "#OO@.....#"
            "#O#.....O#"
            "#O.....OO#"
            "#O.....OO#"
            "#OO....OO#"
            "##########"]
           (grid->chars (sut/follow-path sample)))))


(t/deftest part-1-test
  (t/is (= 1478649 (sut/sum-boxes-gps-after-path input))))


(def part-2-sample
  (->> ["#######"
        "#...#.#"
        "#.....#"
        "#..OO@#"
        "#..O..#"
        "#.....#"
        "#######"
        ""
        "<vv<<^^<<^^"]
       parse-instructions))


(def double-sample (sut/resize-box-map part-2-sample))


(t/deftest double-grid-test
  (t/is (= ["##############"
            "##......##..##"
            "##..........##"
            "##....[][]@.##"
            "##....[]....##"
            "##..........##"
            "##############"]
           (grid->chars (second double-sample)))))

(-> ["####################"
     "##................##"
     "##........@...[]..##"
     "##........[]..[][]##"
     "##........[]...[].##"
     "##................##"
     "####################"]
    parse-double-grid
    (sut/push-double-boxes-v [1 0] [3 10])
    #_(select-keys [[3 10] [4 10] [4 11] [5 10] [5 11]])
    grid->chars
    #_(#(let [poly (sut/find-boxes-polygon % (sut/move->dir \v) [3 10])
            stm? (sut/space-to-move? % poly (sut/move->dir \v) )
            ]
        [poly stm?]))
    )

(t/deftest move-double-robot-test
  (t/is (= ["####################"
            "##................##"
            "##............[]..##"
            "##........@...[][]##"
            "##........[]...[].##"
            "##........[]......##"
            "####################"]
           (-> ["####################"
                 "##................##"
                 "##........@...[]..##"
                 "##........[]..[][]##"
                 "##........[]...[].##"
                 "##................##"
                 "####################"]
                parse-double-grid
                (sut/move-robot-in-dir (sut/move->dir \v))
                grid->chars)))

  (->> "<vv<<^^<<^^"
       (map sut/move->dir)
       (reductions (fn [grid move] (sut/move-robot-in-dir grid move))
                   (second double-sample))
       (drop 1)
       (map grid->chars)
       (= [["##############"
            "##......##..##"
            "##..........##"
            "##...[][]@..##"
            "##....[]....##"
            "##..........##"
            "##############"]
           ["##############"
            "##......##..##"
            "##..........##"
            "##...[][]...##"
            "##....[].@..##"
            "##..........##"
            "##############"]
           ["##############"
            "##......##..##"
            "##..........##"
            "##...[][]...##"
            "##....[]....##"
            "##.......@..##"
            "##############"]
           ["##############"
            "##......##..##"
            "##..........##"
            "##...[][]...##"
            "##....[]....##"
            "##......@...##"
            "##############"]
           ["##############"
            "##......##..##"
            "##..........##"
            "##...[][]...##"
            "##....[]....##"
            "##.....@....##"
            "##############"]
           ["##############"
            "##......##..##"
            "##...[][]...##"
            "##....[]....##"
            "##.....@....##"
            "##..........##"
            "##############"]
           ["##############"
            "##......##..##"
            "##...[][]...##"
            "##....[]....##"
            "##.....@....##"
            "##..........##"
            "##############"]
           ["##############"
            "##......##..##"
            "##...[][]...##"
            "##....[]....##"
            "##....@.....##"
            "##..........##"
            "##############"]
           ["##############"
            "##......##..##"
            "##...[][]...##"
            "##....[]....##"
            "##...@......##"
            "##..........##"
            "##############"]
           ["##############"
            "##......##..##"
            "##...[][]...##"
            "##...@[]....##"
            "##..........##"
            "##..........##"
            "##############"]
           ["##############"
            "##...[].##..##"
            "##...@.[]...##"
            "##....[]....##"
            "##..........##"
            "##..........##"
            "##############"]])
       t/is))


(def bigger-double-sample (sut/resize-box-map sample))

(t/deftest ahh-test
  (t/are [start move expected] (= expected (-> start
                                               parse-double-grid
                                               (sut/move-robot-in-dir (sut/move->dir move))
                                               grid->chars))
    ["################"
     "##............##"
     "##.....[].....##"
     "##....[][]....##"
     "##...[]..[]...##"
     "##....[][]....##"
     "##.....[].....##"
     "##.....@......##"
     "##............##"
     "################"] \^ ["################"
                             "##.....[].....##"
                             "##....[][]....##"
                             "##...[]..[]...##"
                             "##....[][]....##"
                             "##.....[].....##"
                             "##.....@......##"
                             "##............##"
                             "##............##"
                             "################"]
    ["####################"
     "##[]..[]....[]..[]##"
     "##[].......@..[]..##"
     "##........[][][][]##"
     "##....[]..[]..[]..##"
     "##..##....[]......##"
     "##...[].......[]..##"
     "##.....[]..[].[][]##"
     "##........[]......##"
     "####################"] \v ["####################"
                                 "##[]..[]....[]..[]##"
                                 "##[]..........[]..##"
                                 "##.........@[][][]##"
                                 "##....[]..[]..[]..##"
                                 "##..##....[]......##"
                                 "##...[]...[]..[]..##"
                                 "##.....[]..[].[][]##"
                                 "##........[]......##"
                                 "####################"]))

(t/deftest bigger-double-sample-test
  (t/is (= ["####################"
            "##....[]....[]..[]##"
            "##............[]..##"
            "##..[][]....[]..[]##"
            "##....[]@.....[]..##"
            "##[]##....[]......##"
            "##[]....[]....[]..##"
            "##..[][]..[]..[][]##"
            "##........[]......##"
            "####################"]
           (grid->chars (second bigger-double-sample))))
  (t/is (= ["####################"
            "##[].......[].[][]##"
            "##[]...........[].##"
            "##[]........[][][]##"
            "##[]......[]....[]##"
            "##..##......[]....##"
            "##..[]............##"
            "##..@......[].[][]##"
            "##......[][]..[]..##"
            "####################"]
           (grid->chars (sut/follow-path bigger-double-sample)))))


(t/deftest part-2-test
  (t/is (= 9021
           (sut/sum-double-boxes-gps-after-path bigger-double-sample)))
  (t/is (= 1495455
           (sut/sum-double-boxes-gps-after-path (sut/resize-box-map input)))))


(defn setup []
  (q/frame-rate 16)
  (q/background 10)
  (q/text-size 12)
  (->> input
       sut/resize-box-map
       (zipmap [:moves :grid])))

(def factor 15)

(def value->char {:robot [\@ 200 100 121]
                  :wall  [\# 20 20 20]
                  :box-l [\[ 123 111 48]
                  :box-r [\] 100 200 90]
                  :box   [\O 173 131 68]})

(defn update-pos [pt]
  (->> pt
       (map * [factor factor])
       (map + [50 50])
       reverse))

(defn draw [{{:keys [move robot index] :as grid} :grid}]
  (when grid
    (q/background 240)
    (q/fill 0 0 0)

    (q/text (str "Robot: " robot " -move- " move " -index- " index) 10 30)
    (doseq [[pt value] grid
            :when (not (keyword? pt))
            :let [[x y] (update-pos pt)
                  [char r g b] (value->char value)]]
      (q/fill (q/color r g b))
      (condp = char
        \[ (q/rect (+ x 2) (+ y 2) (- (* 2 factor) 4) (- factor 2))
        \] nil
        \# (q/rect (+ x 2) (+ y 2) (- factor 2) (- factor 2))
        (q/text (str char) x y)))

    (let [[x y] (update-pos robot)]
      (q/fill (q/color 200 100 100))
      #_(q/text "日" x y) ;; 🤖
      (q/ellipse x y 15 15))))


(defn update-robots [{[fst & rst] :moves :as state}]
  (when fst
    (-> state
        (assoc :moves rst)
        (update :grid sut/move-robot-in-dir (sut/move->dir fst)))))


(defn -main []
  (q/defsketch robot-map
    :title "Follow the robot"
    :settings #(q/smooth 2)             ;; Turn on anti-aliasing
    :setup setup                        ;; Specify the setup fn
    :draw draw                          ;; Specify the draw fn
    :update update-robots
    :middleware [m/fun-mode]
    :size [1600 1000])
  )

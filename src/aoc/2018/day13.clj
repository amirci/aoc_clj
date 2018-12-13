(ns aoc.2018.day13)

(def pos+dir (partial mapv +))

(defn horizontal
  [pos [dir :as cart]]
  [(pos+dir pos dir) cart])

(def vertical horizontal)

(defn corner-rl
  "Corner top-right and bottom-left
   ?----\\
   |    |
   |    |
  \\----? "
  [pos [dir state]]
  (let [new-dir (reverse dir)]
    [(pos+dir pos new-dir) [new-dir state]]))

(defn corner-lr
  "Corner top-left and bottom-right
  /----?
  |    |
  |    |
  ?----/"
  [pos [dir state]]
  (let [new-dir (->> dir reverse (mapv (partial * -1)))]
    [(pos+dir pos new-dir) [new-dir state]]))

(defn turn-right
  [dir]
  (case dir
    [1  0] [0 1]
    [-1 0] [0 1]
    [0  1] [-1 0]
    [0 -1] [-1 0]))

(defn turn-left
  [dir]
  (case dir
    [1  0] [0  1]
    [-1 0] [0 -1]
    [0  1] [-1 0]
    [0 -1] [1  0]))

(defn intersection-turn
  "left -> straight -> right"
  [[dir state]]
  (case state
    :left     [dir :straight]
    :straight [(turn-right dir) :right]
    [(turn-left  dir) :left]))

(defn intersection
  [pos cart]
  (let [[dir state] (intersection-turn cart)]
    [(pos+dir pos dir) [dir state]]))

(defn parse-char
  [routes carts row col c]
  (case c
    \- [(assoc routes [row col] horizontal  ) carts]
    \+ [(assoc routes [row col] intersection) carts]
    \\ [(assoc routes [row col] corner-rl)    carts]
    \/ [(assoc routes [row col] corner-lr)    carts]
    \| [(assoc routes [row col] vertical    ) carts]
    \> [(assoc routes [row col] horizontal  ) (assoc carts [row col] [[0 1]])]
    \v [(assoc routes [row col] vertical    ) (assoc carts [row col] [[1 0]])]
    \< [(assoc routes [row col] horizontal  ) (assoc carts [row col] [[0 -1]])]
    \^ [(assoc routes [row col] vertical    ) (assoc carts [row col] [[-1 0]])]
    [routes carts]))

(defn parse-instructions
  [lines]
  (reduce
    (fn [[routes carts] [row line]]
      (reduce
        (fn [[routes carts row] [col c]]
          (println ">>> ROW" row "--- COL" col)
          (let [[routes carts] (parse-char routes carts row col c)]
            [routes carts row]))
        [routes carts row]
        (map-indexed vector line)))
    [{} {}]
    (map-indexed vector lines)))



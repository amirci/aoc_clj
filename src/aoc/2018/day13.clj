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

(def d-up    [1  0])
(def d-down  [-1 0])
(def d-right [0  1])
(def d-left  [0 -1])

(defn turn-right [[x y]] [y (* -1 x)])

(defn turn-left [[x y]] [(* -1 y) x])
 
(defn intersection-turn
  "left -> straight -> right"
  [[dir state]]
  (case state
    :left     [dir :straight]
    :straight [(turn-right dir) :right]
    [(turn-left dir) :left]))

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
          (let [[routes carts] (parse-char routes carts row col c)]
            [routes carts row]))
        [routes carts row]
        (map-indexed vector line)))
    [{} {}]
    (map-indexed vector lines)))

(defn tick
  [routes carts]
  (->> carts
       sort
       (reduce
         (fn [new-carts [pos cart]]
           (let [f (routes pos)
                 new-carts (dissoc new-carts pos)
                 [new-pos new-cart] (f pos cart)]
             (if (new-carts new-pos)
               (reduced {:crash new-pos})
               (assoc new-carts new-pos new-cart))))
         carts)))

(defn part-a
  [input]
  (let [[routes carts] (parse-instructions input)]
    (->> carts
         (iterate (partial tick routes))
         (drop-while (comp not :crash))
         first
         :crash
         reverse)))


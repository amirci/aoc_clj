(ns aoc.2018.day11)


(defn hdrd-digit [nbr]
  (mod (quot nbr 100) 10))

(defn power-level*
  "Find the fuel cell's rack ID, which is its X coordinate plus 10.
Begin with a power level of the rack ID times the Y coordinate.
Increase the power level by the value of the grid serial number (your puzzle input).
Set the power level to itself multiplied by the rack ID.
Keep only the hundreds digit of the power level (so 12345 becomes 3; numbers with no hundreds digit become 0).
Subtract 5 from the power level."
  [serial [x y]]
  (let [rack-id (+ x 10)]
    (-> rack-id
        (* y)
        (+ serial)
        (* rack-id)
        hdrd-digit
        (- 5))))


(def power-level (memoize power-level*))


(def width 300)
(def width+ (inc width))

(defn range+ [from to] (range from (inc to)))


(def all-pts
  (for [x (range 1 (inc width)) y (range 1 (inc width))]
    [x y]))


(def empty-sum
  (->> (repeat (vec (int-array (inc width ) 0)))
       (take (inc width ))
       vec))


(defn prev-sum [sum [y x]]
  (- (+ (get-in sum [(dec y) x])
        (get-in sum [y (dec x)]))
     (get-in sum [(dec y) (dec x)])))


(defn summed-area-table
  "https://en.wikipedia.org/wiki/Summed-area_table"
  [serial]
  (reduce
   (fn [sum pos]
     (let [pl (power-level serial (reverse pos))]
       (assoc-in sum
                 pos
                 (+ pl (prev-sum sum pos)))))
   empty-sum
   all-pts))


(defn square-sum [sat size [y x]]
  (let [xs (- x size) ys (- y size)]
    (+ (- (get-in sat [y x] 0)
          (get-in sat [ys x] 0)
          (get-in sat [y xs] 0))
       (get-in sat [ys xs] 0))))


(defn largest-square-of-size [sat size]
  (let [rng (range+ size width)]
    (->> (for [y rng x rng] [y x])
         (reduce
          (fn [best [x y]]
            (->> (square-sum sat size [y x])
                 (vector (- x size -1) (- y size -1) size)
                 (max-key last best)))
          [0 0 size Integer/MIN_VALUE]))))


(defn largest-all [serial]
  (let [sat (summed-area-table serial)]
    (->> (range 1 30)
         (map (partial largest-square-of-size sat))
         (apply max-key last))))



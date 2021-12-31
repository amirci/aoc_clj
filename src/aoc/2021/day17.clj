(ns aoc.2021.day17)

(defn n-sum [n]
  (quot (* n (inc n)) 2))


(defn highest [[_ y-rng]]
  (let [vy (Math/abs (second y-rng))]
    (n-sum (dec vy))))


(defn findx-candidates
  [[low high]]
  (let [ll (* low 2)]
    (->> (range (inc high))
         (filter (fn [n] (<= ll (* n (inc n))))))))


(defn step-y [[n v i]]
  [(+ n v) (dec v) (inc i)])


(defn step-x [[_ vx :as pos]]
  (cond-> pos
    (pos? vx) step-y))


(defn ity [x] (iterate step-y [x (dec x) 0]))


(defn itx [x] (iterate step-x [x (dec x) 0]))


(defn x-in-target
  [vx [x1 x2]]
  (->> (itx vx)
       (drop-while #(< (first %) x1))
       (take-while #(<= (first %) x2))))


(defn in-range?
  [[[x1 x2] [y1 y2]] [x y]]
  (and (<= x1 x x2) (<= y2 y y1)))


(defn intersects? [[_ y-rng :as target] c xs vy]
  (->> (ity vy)
       (drop c)
       (take-while #(<= (second y-rng) (first %)))
       (map vector xs)
       (map (partial map first))
       (filter (partial in-range? target))
       first))


(defn all-ys [[x-rng [_ y2] :as target] s vx]
  (let [[[_ _ c] :as xs] (x-in-target vx x-rng)]
    (if-not c
      s
      (->> (range y2 (inc (* -1 y2)))
           (filter (partial intersects? target c xs))
           (map (partial vector vx))
           set
           (clojure.set/union s)))))


(defn all-vs [[x-rng :as target]]
  (reduce
   (partial all-ys target)
   #{}
   (findx-candidates x-rng)))


(def count-all-vs (comp count all-vs))

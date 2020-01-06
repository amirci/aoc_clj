(ns aoc.2019.day10)

(defn ->vec
  [[a b] [c d]]
  [(- c a) (- d b)])

(defn slope
  [p1 p2]
  (let [[d1 d2] (->vec p1 p2)]
    (if (zero? d1)
      :infinity
      (double (/ d2 d1)))) )

(defn square [x] (* x x))

(def sqrt #(java.lang.Math/sqrt %))

(defn magnitude
  ([v] (->> v (map square) (apply +) sqrt))
  ([p1 p2] (magnitude (->vec p1 p2))))

(defn collinear?
  ([[a b] [c d]]
   {:pre [(and (pos? c) (pos? d))]}
   (= (/ a c) (/ b d)))
  ([pt1 pt2 pt3]
   (collinear? (->vec pt1 pt2) (->vec pt1 pt3))))

(defn dotprod
  "Ax * Bx + Ay * By "
  ([[a b :as v1]
    [c d :as v2]] (+ (* a c) (* b d)))
  ([pt1 pt2 pt3]
   (dotprod (->vec pt1 pt2) (->vec pt1 pt3))))

(defn angle
  [a b c]
  (/ (dotprod a b c)
     (* (magnitude a b) (magnitude a c))))

(def same-vector? (comp pos? angle))

(defn in-between?
  "Given all points on the same line, is b between a and c?"
  [a b c]
  (< (magnitude a b) (magnitude a c)))

(defn replace-pt
  [p1 p2 coll]
  (-> coll
      (disj p1)
      (conj p2)))


(defn closest-in-sight
  [target ast closest-so-far]
  (let [[c1 c2] (vec closest-so-far)
        sv1? (and c1 (same-vector? target c1 ast))
        sv2? (and c2 (same-vector? target c2 ast))]
    (cond
      (not c1) #{ast}
      (and sv1? (in-between? target ast c1)) (replace-pt c1 ast closest-so-far)
      (and (not c2) (not sv1?)) (conj closest-so-far ast)
      (and sv2? (in-between? target ast c2)) (replace-pt c2 ast closest-so-far)
      :else closest-so-far)))


(defn in-sight
  [target coll]
  (reduce
   (fn [m ast]
     (let [s (slope target ast)
           found (get m s)]
       (assoc m s (closest-in-sight target ast found))))
   {}
   (disj coll target)))

(defn direct-lines
  [asteroids]
  (reduce
   (fn [m pt]
     (->> asteroids
          (in-sight pt)
          (assoc m pt)))
   {}
   asteroids))

(defn best-base-location
  [asteroids]
  (->> asteroids
       direct-lines
       (map (fn [[pt m]]
              [pt (apply + (map count (vals m)))]))
       (apply max-key second)))

(ns aoc.2019.day10)

(defn ->vec
  [[a b] [c d]]
  [(- c a) (- d b)])

(defn square [x] (* x x))

(def sqrt #(java.lang.Math/sqrt %))

(defn magnitude
  ([v] (->> v (map square) (apply +) sqrt))
  ([p1 p2] (magnitude (->vec p1 p2))))

(defn dotprod
  "Ax * Bx + Ay * By "
  ([[a b :as v1]
    [c d :as v2]] (+ (* a c) (* b d)))
  ([pt1 pt2 pt3]
   (dotprod (->vec pt1 pt2) (->vec pt1 pt3))))

(defn angle-cos
  [a b c]
  (/ (dotprod a b c)
     (* (magnitude a b) (magnitude a c))))

(defn angle
  [a b c]
  (java.lang.Math/acos (angle-cos a b c)))

(defn angle-360
  [[x1 :as a] b [x2 :as c]]
  (let [ang (float (Math/toDegrees (angle a b c)))]
    (if (< x2 x1)
      (+ 180 (- 180 ang))
      ang)))

(defn in-sight
  [center coll]
  (reduce
   (fn [m new-ast]
     (let [b (assoc center 1 -1)
           ang (angle-360 center b new-ast)
           old-ast (get m ang)
           distance (partial magnitude center)]
       (assoc m ang
              (if old-ast
                (min-key distance new-ast old-ast)
                new-ast))))
   {}
   (disj coll center)))

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
       (map (juxt first (comp count second)))
       (apply max-key second)))


; Part b
(defn sort-by-distance-to
  [center]
  (partial sort-by (partial magnitude center)))

(defn same-angle-from
  [[a b :as center]]
  (partial angle-360 center [a -1]))

(defn in-full-sight
  [center asteroids]
  (->> (disj asteroids center)
       (group-by (same-angle-from center))
       (map #(update % 1 (sort-by-distance-to center)))
       (into (sorted-map))))

(defn vaporize-asteroid
  [[vaporized pending] [ang [target & rst]]]
  [(conj vaporized target)
   (cond-> pending
     (seq rst) (assoc ang rst))])

(defn vaporize-round
  [pending]
  (reduce vaporize-asteroid [[] (sorted-map)] pending))

(defn vaporize-seq
  [pending]
  (let [[vaporized pending] (vaporize-round pending)]
    (lazy-seq (concat vaporized (vaporize-seq pending)))))

(def vaporize-all (comp vaporize-seq in-full-sight))


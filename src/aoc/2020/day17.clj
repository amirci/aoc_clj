(ns aoc.2020.day17)

(defn ->cell [z x y c]
  (when (= \# c)
    [x y z]))

(defn ->row [z i row]
  (->> row
       (map-indexed (partial ->cell z i))
       (filter seq)
       set))

(defn ->grid [input]
  (->> input
       (map-indexed (partial ->row 0))
       (apply clojure.set/union)))


(defn ->grid4 [input]
  (->> input
       ->grid
       (map #(conj % 0))
       set))


(def diff [-1 0 1])


(def around
  (for [x diff y diff z diff :when (not= [0 0 0] [x y z])]
    [x y z]))


(defn neighbours [dim cube]
  (map (partial map + cube) dim))


(defn alive? [grid [cube ncount]]
  "If a cube is active and exactly 2 or 3 of its neighbors are also active, the cube remains active. Otherwise, the cube becomes inactive.
   If a cube is inactive but exactly 3 of its neighbors are active, the cube becomes active. Otherwise, the cube remains inactive."
  (cond
    (= 3 ncount) cube
    (and (= 2 ncount) (grid cube)) cube))


(defn cycle-step [dim grid]
  (->> grid
       (mapcat (partial neighbours dim))
       frequencies
       (filter (partial alive? grid))
       (map first)
       set))


(defn boot-cycle* [stepFn grid]
  (->> grid
       (iterate stepFn)
       (drop 6)
       first))

(defn boot-cycle [input]
  (->> input
       ->grid
       (boot-cycle* (partial cycle-step around))))


(def around4
  (for [x diff y diff z diff w diff
        :when (not= [0 0 0 0] [x y z w])]
    [x y z w]))

(defn boot-cycle4 [input]
  (->> input
       ->grid4
       (boot-cycle* (partial cycle-step around4))))



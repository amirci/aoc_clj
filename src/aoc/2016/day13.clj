(ns aoc.2016.day13
  (:require [clojure.tools.trace :refer [trace]]
            [clojure.string :refer [join starts-with? split]]))

(def favorite-nbr 1352)

(defn count-ones
  [coll]
  (->> coll
       (filter #(= % \1))
       count))

(defn calc-maze*
  [x y nbr]
  (-> (+ (* x x) (* 3 x ) (* 2 x y) y (* y y))
      (+ nbr)
      (Integer/toString 2)
      count-ones
      even?
      (if :space :wall)))

(def calc-maze (memoize calc-maze*))

(defn possible-moves
  [[x y] nbr]
  (for [a (range -1 2) b (range -1 2)
        :let [[c d] (map + [x y] [a b])]
        :when (and (not= [c d] [x y])
                   (>= c 0)
                   (>= d 0)
                   (or (= c x)
                       (= d y))
                   (= :space (calc-maze c d nbr)))]
    [c d]))

(def empty-queue clojure.lang.PersistentQueue/EMPTY)

(defn find-path
  [start pred nbr]
  (loop [distances {start 0} pending (conj empty-queue start)]
    (if (empty? pending)
      [0 distances]
      (let [pt (peek pending) pending (pop pending) distance (distances pt)]
        (if (pred pt distance)
          [distance distances]
          (let [visited (set (keys distances))
                moves (-> (possible-moves pt nbr) set (clojure.set/difference visited))
                new-distance (inc distance)
                distances (reduce #(assoc %1 %2 new-distance) distances moves)
                pending (reduce #(conj %1 %2) pending moves)]
            (recur distances pending)))))))


(defn distance-to
  [start end nbr]
  (let [found-it (fn [pt _] (= pt end))]
    (first (find-path start found-it nbr))))

(defn all-locations
  [start limit nbr]
  (let [prune-it (fn [_ distance] (= distance (inc limit)))]
    (->> (find-path start prune-it nbr)
         last
         vals
         (filter #(<= % limit))
         count)))

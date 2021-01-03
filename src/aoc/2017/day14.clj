(ns aoc.2017.day14
  (:require
   [aoc.2017.day10 :as d10]
   [clojure.pprint :refer [cl-format]]
   [clojure.tools.trace :refer [trace]]
   [clojure.tools.trace :as tr]))


(defn knot-row
  [word row]
  (->> (str word "-" row)
       d10/knot
       (map #(Integer/bitCount (Integer/parseInt (str %) 16)))
       (apply +)))


(defn count-squares [word]
  (->> (range 128)
       (map (partial knot-row word))
       (apply +)))


(defn mk-grid [word]
  (->> (range 128)
       (map (partial str word "-"))
       (map d10/knot-base)
       (mapv (fn [kb] (->> kb
                          (map #(cl-format nil "~8,'0b" %))
                          (apply str)
                          (replace {\1 \# \0 \.})
                          vec)))))


(def around [[0 1] [1 0] [0 -1] [-1 0]])


(defn occupied? [grid loc]
  (= \1 (get-in grid loc)))


(defn grid-coords [grid]
  (for [x (range (count grid))
        y (range (count (first grid)) )] [x y]))


(defn conj-if [coll test v]
  (if test
    (conj coll v)
    coll))


(defn neighbours [loc]
  (->> around
       (map #(map + loc %))))


(defn occupied#? [grid loc]
  (= \# (get-in grid loc)))


(defn add-occupied-nbrs [pending grid visited loc]
  (->> loc
       neighbours
       (filter (partial occupied#? grid))
       set
       (#(clojure.set/difference % visited))
       (apply conj pending)))


(defn find-region-step [{:keys [grid pending visited] :as state}]
  (let [[loc & rst] pending
        occ? (occupied#? grid loc)]
    (-> state
        (update :visited conj loc)
        (update :current conj-if occ? loc)
        (update :pending add-occupied-nbrs grid visited loc)
        (update :pending disj loc))))


(defn add-region [{:keys [current] :as state}]
  (update state :regions conj current))


(defn find-region [{:keys [visited] :as state} loc]
  (if (visited loc)
    state
    (->> (assoc state :current #{} :pending #{loc})
         (iterate find-region-step)
         (drop-while (comp seq :pending))
         first
         add-region)))


(defn init-state [grid]
  {:regions #{} :visited #{} :grid grid})


(defn find-regions [grid]
  (->> grid
       grid-coords
       (filter (partial occupied#? grid))
       (reduce find-region (init-state grid))
       :regions))


(defn count-regions [input]
  (->> input
       mk-grid
       find-regions
       count))


(defn mark-region [grid [i region]]
  (reduce
   (fn [m pos]
     (assoc-in m pos i))
   grid
   region))


(defn mark-regions [grid regions]
  (->> regions
       (map-indexed vector)
       (reduce
        mark-region
        grid)))

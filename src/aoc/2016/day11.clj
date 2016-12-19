(ns aoc.2016.day11
  (:require [clojure.tools.trace :refer [trace]]
            [clojure.set :refer [intersection difference]]
            [clojure.string :refer [ends-with? starts-with? split]]))


(defn print-floor
  [f all]
  (let [floor? (set f)]
    (doseq [e all]
      (print (format "%5s" (if (floor? e) (name e) "."))))))

(defn print-spec
  [[e & floors]]
  (let [all (sort-by name (apply concat floors))
        floors (reverse floors)]
    (loop [[f & rst] floors i 4]
      (when f
        (print (str "F" i " "))
        (print (if (= e i) "E" "."))
        (print-floor f all)
        (println)
        (recur rst (dec i)))))
  (println))

(def empty-queue clojure.lang.PersistentQueue/EMPTY)

(defn tail
  [k]
  (-> k name (.substring 1) keyword))

(defn micros
  [floor]
  (->> floor (filter #(starts-with? (name %) "m")) (map tail) set))

(defn generators
  [floor]
  (->> floor (filter #(starts-with? (name %) "g")) (map tail) set))

(defn chip-match-gen?
  [floor]
  (let [ms (micros floor) gs (generators floor)
        common (intersection ms gs)]
    (or (= common ms)
        (empty? ms)
        (empty? gs))))

(defn will-survive?
  [floor]
  (fn [pair] (chip-match-gen? (concat floor pair))))

(defn valid-pairs
  [floor]
  (->> (for [a floor b floor :when (and (not= a b))] (sort [a b]))
       set
       (filter chip-match-gen?)))

(defn possible-moves-between-floors
  [n src tgt]
  (let [survivor? (will-survive? tgt)
        pairs (->> src valid-pairs)
        singles (->> src (map vector))]
    (->> singles
         (concat pairs)
         (map (fn [p] [(remove (set p) src) (apply conj tgt p)]))
         (filter (fn [[src tgt]] (and (chip-match-gen? src) (chip-match-gen? tgt)))))))

(defn update-rows
  [spec e1 e2]
  (fn [[f g]]
    (-> spec
        (assoc 0 e2)
        (assoc e1 (set f))
        (assoc e2 (set g)))))

(defn possible-down
  [[e & rst :as spec]]
  (if (= e 1)
    []
    (let [prev (dec e)
          moves (possible-moves-between-floors 2 (nth spec e) (nth spec prev))]
      (map (update-rows spec e prev) moves))))

(defn possible-up
  [[e & rst :as spec]]
  (if (= e 4)
    []
    (let [nxt (inc e)
          moves (possible-moves-between-floors 2 (nth spec e) (nth spec nxt))]
      (map (update-rows spec e nxt) moves))))

(defn possible-moves
  [spec]
  (->> spec
       ((juxt possible-up possible-down))
       (apply concat)))


(defn everything-is-on-the-4th-floor?
  [[e f1 f2 f3]]
  (every? empty? [f1 f2 f3]))

(defn move-stuff-around
  [[min-so-far visited pending]]
  (if (empty? pending)
    [min-so-far visited nil]
    (let [spec (peek pending) pending (pop pending) [steps _ :as visited?] (visited spec)]
      (cond
        (everything-is-on-the-4th-floor? spec) [(min min-so-far steps) visited pending]
        (>= steps min-so-far) [min-so-far visited pending]
        :else (let [moves (->> spec possible-moves (filter (complement visited)))
                    dist (-> (visited spec) (as-> [d p] [(inc d) (conj p spec)]))
                    visited (reduce #(assoc %1 %2 dist) visited moves)]
                [min-so-far visited (apply conj pending moves)])))))

(defn all-possibilities
  [floor-spec]
  (iterate move-stuff-around [75 {floor-spec [0 []]} (conj empty-queue floor-spec)]))

(defn shortest-top-floor
  [spec]
  (->> spec
       all-possibilities
       (drop-while (comp not empty? last))
       first
       first))


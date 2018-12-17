(ns aoc.2018.day17
  (:require 
    [clojure.string :as str]))

(defn fill-clay
  [clay [a n1 b n2 n3 :as qq]]
  (try
    (let [r1 (range n1 (inc n1))
          r2 (range n2 (inc n3))
          [x-range y-range] (if (= a :x) [r1 r2] [r2 r1])]
      (->> (for [x x-range y y-range] [x y])
           set
           (clojure.set/union clay)))
    (catch Exception e
      (println ">>> EXCEPTION" qq)
      clay)))

(def calc-clay (partial reduce fill-clay #{}))

(defn calc-boundaries
  [points]
  (reduce
    (fn [[min-x min-y max-x max-y] [x y]]
      [(min min-x x)
       (min min-y y)
       (max max-x x)
       (max max-y y)])
    [Integer/MAX_VALUE Integer/MAX_VALUE Integer/MIN_VALUE Integer/MIN_VALUE]
    points))

(defn neighbours
  [[a b]]
  (for [x [-1 0 1] y [-1 0 1] :when (not= [0 0] [x y])]
    [(+ a x) (+ b y)]))

(defn side-neighbours
  [[a b]]
  [[(dec a) b]
   [a (inc b)]
   [(inc a) b] ])

(defn sand?
  [pos clay water]
  (and 
    (nil? (clay pos))
    (nil? (water pos))))

(defn flood
  ([pos clay] (flood pos clay #{}))
  ([pos clay water]
   (if (sand? pos clay water)
     (let [[nl bd nr] (side-neighbours pos)
           water* (conj water pos)
           water* (flood nl clay water*)
           ;water* (flood br clay water*)
           ]
       water*)
     water)))

(defn de-pop
  [pending]
  [(last pending) (pop pending)])

(defn find-wall
  [rng clay y]
  (->> (for [x rng] [x y])
       (filter clay)
       first))

(defn closed-walls-below?
  [[a b] clay water]
  (let [[x y :as pos] [a (inc b)]
        [min-x _ max-x] (calc-boundaries clay)
        [wlx] (find-wall (range x (dec min-x) -1) clay y)
        [wrx] (find-wall (range x (inc max-x)) clay y)]
    (and wlx 
         wrx
         (every? water (for [x (range wlx (inc wrx))] [x y])))))

(defn watery?
  [pos clay water]

  (when (= pos [499 1])
    (println ">> BELL?" (closed-walls-below? pos clay)))
           
  (and (sand? pos clay water)
       (closed-walls-below? pos clay water)))

(defn flood2
  [[clay water pending]]
  (let [[pos pending*] (de-pop pending)]
    (if (water pos)
      [clay water pending*]
      (let [[nl bd nr] (side-neighbours pos)
            [water* pending*] (if (watery? pos clay water) 
                                [(conj water pos) (conj pending* nl bd)]
                                [water pending*])]
        [clay water* pending*]))))



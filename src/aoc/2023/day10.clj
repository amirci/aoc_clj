(ns aoc.2023.day10)

(defn mk-pipe [[c d] [e f]]
  (fn [[a b]]
    (let [before [(+ a c) (+ b d)] after [(+ a e) (+ b f)]]
      {before after after before})))


(def h-pipe (mk-pipe [0 -1] [0 1]))

(def v-pipe (mk-pipe [-1 0] [1 0]))

(def j-pipe (mk-pipe [0 -1] [-1 0]))

(def f-pipe (mk-pipe [1 0] [0 1]))

(def seven-pipe (mk-pipe [0 -1] [1 0]))

(def l-pipe (mk-pipe [0 1] [-1 0]))

(def pipes {\| v-pipe
            \- h-pipe
            \J j-pipe
            \L l-pipe
            \F f-pipe
            \7 seven-pipe
            \S identity})

(def pipe? (set (keys pipes)))


(defn pipes->pos [lines]
  (->> (for [x (range (count lines)) y (range (count (first lines )))
             :let [c (get-in lines [x y])]
             :when (pipe? c)]
         [[x y] c])))


(defn parse-pipes [lines]
  (->> lines
       pipes->pos
       (reduce (fn [m [pos pipe]]
                 (cond-> m
                   (= \S pipe) (assoc :start pos)
                   (pipes pipe) (assoc-in [:config pos] (-> pipe pipes (apply [pos])))
                   :always (assoc-in [:source pos] pipe)))
               {:config {} :start nil :source {}})))



(defn follow-start? [source [start-x start-y] [x y]]
  (cond-> (source [x y])
    (< start-x x) #{\| \L \J}
    (< x start-x) #{\| \F \7}
    (< start-y y) #{\- \7 \J}
    (< y start-y) #{\- \F \L}))


(defn starting [{:keys [source] [a b] :start}]
  (->> (for [x [-1 0 1] y [-1 0 1] :when (not= (Math/abs x) (Math/abs y))]
         [(+ a x) (+ b y)])
       (filter source)
       (filter (partial follow-start? source [a b]))
       first))

(defn mk-state [pipes]
  (assoc pipes :path (starting pipes)))

(defn follow-pipe [config [pos prev steps]]
  (when-let [f (config pos)]
    (when-let [next-pos (f prev)]
        [next-pos pos (conj steps pos)])))


(defn not-back-to-start? [start [pos]]
  (not= start pos))


(defn find-loop [{:keys [config start path]}]
  (->> [path start []]
       (iterate (partial follow-pipe config))
       (drop-while (partial not-back-to-start? start))
       first
       last))


(defn find-loop-steps [pipes]
  (->> pipes
       find-loop
       count
       inc
       (#(quot % 2))))


(defn furthest-point [input]
  (->> input
       parse-pipes
       mk-state
       find-loop-steps))


(defn walls-segments
  "Calculate the walls on the line
  For example:
    * - is not a walls
    * Having 7 is not a wall if there's a L before
    * Having J is not a wall if there's a F before
    * | is a wall
  "
  [source points]
  (->> points
       (remove (comp (partial = \-) source))
       (reduce (fn [found pt]
                 (let [prev (last found)
                       pair [(source prev) (source pt)]]
                   (cond
                     (= [\L \7] pair) found
                     (= [\F \J] pair) found
                     :else (conj found pt))))
               [])
       (map (fn [pt] (conj pt (source pt))))
       (partition 2 1)))


(defn- count-tiles-between [p-loop source [[a b] [_ c]]]
  (->> (for [x (range (inc b) c)
             :let [pt (source [a x])]
             :when (or (nil? pt)
                       (and (not= \S pt)
                            (not (p-loop [a x]))))]
         [a x])
       count))


(defn count-line-enclosed
  [p-loop {:keys [source]} points]
  (->> points
       sort
       (walls-segments source)
       (map-indexed vector)
       (filter (comp even? first))
       (map second)
       (map (partial count-tiles-between p-loop source))
       (apply +)))



(defn enclosed-count [input]
  (let [pipes (-> input parse-pipes)
        p-loop (-> pipes mk-state find-loop set)]
    (->> p-loop
         (group-by first)
         vals
         (map (partial count-line-enclosed p-loop pipes))
         (apply +))))

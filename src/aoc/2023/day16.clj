(ns aoc.2023.day16)


(def dirs {:u [-1 0] :d [1 0]
           :l [0 -1] :r [0 1]})

(defn mk-beam [pt d] [pt (cond-> d (keyword? d) (dirs))])


(defn sum-pt [pt] (partial mapv + pt))

(def left (sum-pt (dirs :l)))

(def right (sum-pt (dirs :r)))

(def up (sum-pt (dirs :u)))

(def down (sum-pt (dirs :d)))


(def advance-beam
  (constantly
   (fn [[pos dir]] (mk-beam ((sum-pt pos) dir) dir))))

(def horizontal? (comp zero? first))

(def vertical? (comp zero? second))

(defn split-dash
  "Split using a -
  * Left <-> Right
  * Up -> [Left, Right]
  * Down -> [Left, Right] "
  [pt]
  (let [l (left pt) r (right pt)
        splitted [:split
                  (mk-beam l [0 -1])
                  (mk-beam r [0 1])]]
    (fn [[pt2 dir]]
      (if (horizontal? dir)
        ((advance-beam) [pt2 dir])
        splitted))))


(defn split-pipe
  [pt]
  (let [u (mk-beam (up pt) [-1 0])
        d (mk-beam (down pt) [1 0])]
    (fn [[pt2 dir]]
      (if (vertical? dir)
        ((advance-beam) [pt2 dir])
        [:split u d]))))


(defn reflect-90-forward
  "Using a / mirror
  * Going Left <-> Down
  * Going Right <-> Up"
  [pt]
  (let [[u d l r] ((juxt up down left right) pt)
        dir-map {[0 1] [u [-1 0]]
                 [1 0] [l [0 -1]]
                 [0 -1] [d [1 0]]
                 [-1 0] [r [0 1]]}]
    (fn [[_ dir]] (dir-map dir))))


(defn reflect-90-backward
  "Using a \\ mirror
  * Going Right <-> Down
  * Going Left <-> Up"
  [pt]
  (let [[u d l r] ((juxt up down left right) pt)
        dir-map {(dirs :l) [u (dirs :u)]
                 (dirs :d) [r (dirs :r)]
                 (dirs :r) [d (dirs :d)]
                 (dirs :u) [l (dirs :l)]}]
    (fn [[_ dir]] (dir-map dir))))




(def tile->fn
  {\. advance-beam
   \\ reflect-90-backward
   \/ reflect-90-forward
   \- split-dash
   \| split-pipe})


(defn load-tile-map [tiles]
  (->> (for [x (range (count tiles))
             y (range (count (first tiles)))
             :let [tile (get-in tiles [x y])]]
         [[x y] [((tile->fn tile) [x y]) tile]])
       (into {})))


(defn follow-beam
  [tiles [beam [fst & rst :as pending] visited]]
  (let [[[pt :as beam] pending] (if beam [beam pending] [fst rst])
        [f] (tiles pt)]
    (cond
      (or
       (nil? f)
       (visited beam)) [(first pending) (rest pending)
                        visited]
      :else (let [[k b1 b2 :as new-beam] (f beam)
                  visited (conj visited beam)]
              (cond-> []
                (= :split k) (conj b1 (conj pending b2))
                (not= :split k) (conj new-beam pending)
                :always (conj visited))))))

(defn- not-energized? [[beam pending]]
  (or beam (not-empty pending)))

(defn energize [start tiles]
  (->> [start [] #{}]
       (iterate (partial follow-beam tiles))
       (drop-while not-energized?)
       first
       last))

(defn count-energized-tiles
  ([tiles] (count-energized-tiles (load-tile-map tiles)
                                  (mk-beam [0 0] :r)))
  ([tiles start] (->> tiles
                      (energize start)
                      (map first)
                      set
                      count)))


(defn- create-beams [rows cols [x y :as pt]]
  (cond-> []
    (zero? x) (conj :d)
    (zero? y) (conj :r)
    (= (dec rows) x) (conj :u)
    (= (dec cols) y) (conj :l)
    :always (#(map (partial mk-beam pt) %))))


(defn find-entry-points [tiles]
  (let [cols (count (first tiles))
        rows (count tiles)]
    (->> (for [x (range 1 (dec rows))
               y [0 (dec cols)]]
           [x y])
         (concat
          (for [x [0 (dec rows)] y (range cols)] [x y]))
         (filter #(= \. (get-in tiles %)))
         (mapcat (partial create-beams rows cols)))))


(defn max-energized-tiles [tiles]
  (let [edges (find-entry-points tiles)
        tiles (load-tile-map tiles)]
    (->> edges
         (pmap (partial count-energized-tiles tiles))
         (apply max))))

(ns aoc.2017.day21)

(def flip-h reverse)


(defn rotate [tile]
  (->> tile
       (apply mapv vector)
       (map reverse)))


(defn permutations [tile]
  (let [rotated (->> tile (map seq) (iterate rotate) (take 4))]
    (->> rotated
         (map flip-h)
         (concat rotated)
         distinct
         (map vec)
         set)))


(defn ->square [s]
  (vec
   (clojure.string/split s #"/")))

(defn ->squares [[in _ out]]
  (mapv ->square [in out]))

(defn parse-rule [line]
  (let [[in out] (->> (clojure.string/split line #" " )
                      ->squares)]
    [(permutations in) out]))


(defn parse-rules [input]
  (map parse-rule input))


(defn build-square [width grps]
  (->> grps
       (map (partial partition width))
       (apply map vector)))


(defn split-squares [width image]
  (->> image
       (partition width)
       (mapcat (partial build-square width))))


(defn enhance-image [rules image]
  (->> rules
       (filter (fn [[in out]] (in image)))
       first
       second))


(defn join-row [row]
  (apply map concat row))


(defn join-squares [width squares]
  (->> squares
       (partition-all width)
       (mapcat join-row)))


(defn enhance-step [rules drawing]
  (let [size (count drawing)
        width (if (even? size) 2 3)]
    (->> drawing
         (split-squares width)
         (map (partial enhance-image rules))
         (join-squares (quot size width)))))


(defn enhance [drawing rules rounds]
  (let [rules (parse-rules rules)]
    (->> drawing
         (iterate (partial enhance-step rules))
         (drop rounds)
         first)))

(def active? (partial = \#))

(defn count-active-pixels [drawing rules rounds]
  (->> (enhance drawing rules rounds)
       (map (comp count (partial filter active?)))
       (apply +)))

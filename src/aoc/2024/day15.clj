(ns aoc.2024.day15)


(def add-pt (partial mapv +))


(def wall? (partial = :wall))


(def box? #{:box :box-l :box-r})


(def free? nil?)


(defn- walk-past-boxes [grid dir pos]
  (->> pos
       (iterate (partial add-pt dir))
       (drop 1)
       (drop-while (comp box? grid))
       first
       (#(when (free? (grid %)) %))))


(defn assign-robot [grid pos]
  (when grid
    (-> grid
        (assoc :robot pos)
        (dissoc pos))))


(defn push-single-boxes [grid dir pos]
  (->> (walk-past-boxes grid dir pos)
       ((fn [new-pos]
          (when new-pos
            (-> grid
                (assoc new-pos :box)
                (assign-robot pos)))))))


(defn- move-double-boxes-h
  [grid [x1 y1] [_ y-dir] [_ y2 :as new-pos]]
  (when new-pos
    (let [[from to] (sort [y1 y2])
          from (cond-> from (pos? y-dir) inc)]
      (->> (range from to 2)
           (mapcat (fn [y] [[[x1 y] :box-l]
                            [[x1 (inc y)] :box-r]]))
           (reduce (fn [grid [pos char]] (assoc grid pos char))
                   grid)))))

(def box-l? (partial = :box-l))

(def right [0 1])

(def left [0 -1])

(defn find-boxes-polygon [grid dir start]
  (loop [[fst & rst] [start] visited? (sorted-set)]
    (let [content (grid fst)]
      (cond
        (nil? fst) visited?
        (visited? fst) (recur rst visited?)
        (box? content) (recur (conj rst
                                    (add-pt fst (if (box-l? content) right left))
                                    (add-pt fst dir))
                              (conj visited? fst))
        (wall? content) nil
        :else (recur rst visited?)))))


(def up? (partial = [-1 0]))


(defn- same-row? [row [x]] (= row x))


(defn space-to-move? [grid boxes dir]
  (let [[target-row] (if (up? dir) (first boxes) (last boxes))]
    (->> boxes
         (filter (partial same-row? target-row))
         (map (partial add-pt dir))
         (every? (comp free? grid)))))

(defn cmp-function [dir]
  (if (up? dir) compare #(compare %2 %1)))

(defn push-double-boxes-v [grid dir pos]
  (when-let [poly (find-boxes-polygon grid dir pos)]
    (when (space-to-move? grid poly dir)
      (->> poly
           (map (juxt identity (partial add-pt dir) grid))
           (sort-by first (cmp-function dir))
           (reduce (fn [new-grid [old-pos new-pos box]]
                     (-> new-grid
                         (dissoc old-pos)
                         (assoc new-pos box)))
                   grid)))))


(defn push-double-boxes-h [grid dir pos]
  (let [dir (mapv * dir [0 2])]
    (->> (walk-past-boxes grid dir pos)
         (move-double-boxes-h grid pos dir))))


(def horizontal? (comp zero? first))


(defn push-double-boxes [grid dir pos]
  (let [f (if (horizontal? dir) push-double-boxes-h push-double-boxes-v)]
    (-> grid
        (f dir pos)
        (assign-robot pos))))


(defn- assert-right-move [old-robot dir {:keys [robot] :as new-grid }]
  (assert (= robot (add-pt old-robot dir)) "Robot did not move in the expected direction")
  new-grid)


(defn move-robot-in-dir [{:keys [robot push-boxes] :as grid} dir]
  (let [new-pos (add-pt robot dir)
        push-boxes (or push-boxes push-single-boxes)
        char (grid new-pos)]
    (-> (cond
          (wall? char) grid
          (free? char) (assign-robot grid new-pos)
          (box? char) (if-let [new-grid (push-boxes grid dir new-pos)]
                        (assert-right-move robot dir new-grid)
                        grid))
        (assoc :move dir)
        (update :index (fnil inc 0)))))


(def move->dir {\^ [-1 0] \v [1 0] \< [0 -1] \> [0 1]})


(defn follow-path [[moves grid]]
  (->> moves
       (map move->dir)
       (reduce move-robot-in-dir grid)))


(defn sum-boxes-gps-after-path
  ([input] (sum-boxes-gps-after-path box? input))
  ([box-f? input]
   (->> input
        follow-path
        (filter (comp box-f? second))
        (map first)
        (map (fn [[row col]] (+ col (* row 100))))
        (apply +))))


(def sum-double-boxes-gps-after-path
  (partial sum-boxes-gps-after-path box-l?))


(defn resize-box-map [[moves grid]]
  (->> grid
       (filter (comp (complement keyword?) first))
       (mapcat (fn [[[row col] kw]]
                 (let [new-col (* 2 col)
                       [e1 e2] (if (= :wall kw)
                                 [:wall :wall]
                                 [:box-l :box-r])]
                         [[[row new-col] e1]
                          [[row (inc new-col)] e2]])))
       (into {})
       (merge (select-keys grid [:rows :cols :robot]))
       (#(-> %
             (update :cols * 2)
             (update :robot update 1 * 2)
             (assoc :push-boxes push-double-boxes)))
       (vector moves)))

(ns aoc.2020.day11)

(def around
  (for [x [-1 0 1] y [-1 0 1]
        :when (not= [0 0] [x y])]
    [x y]))


; If a seat is empty (L) and there are no occupied seats adjacent to it, the seat becomes occupied.
; If a seat is occupied (#) and four or more seats adjacent to it are also occupied, the seat becomes empty.
; Otherwise, the seat's state does not change.

(defn all-seats [seats]
  (for [row (range 0 (count seats))
        col (range 0 (count (first seats)))]
    [row col]))


(defn adjacent [seats seat]
  (->> around
       (map #(map + seat %))
       (map #(get-in seats % \.))
       (filter (partial = \#))
       count))

(defn seating-rule [seat adj]
  (cond
    (and (= \L seat) (zero? adj)) \#
    (and (= \# seat) (< 3 adj)) \L
    :else seat))

(defn seating-adj [seats]
  (reduce
   (fn [result coord]
     (let [adj (adjacent seats coord)]
       (update-in result coord seating-rule adj)))
   seats
   (all-seats seats)))

(defn seat-people [{:keys [fn current prev] :as state}]
  (-> state
      (assoc :prev current)
      (update :current fn)))

(defn changed? [{:keys [current prev]}]
  (not= current prev))

(def occupied? (partial = \#))

(defn seating-area* [f seats]
  (->> seats
       (mapv vec)
       (assoc {:fn f} :current)
       (iterate seat-people)
       (drop-while changed?)
       first
       :current
       (mapcat identity)
       (filter occupied?)
       count))

(def seating-area (partial seating-area* seating-adj))

(def seat-empty? (partial = \L))

(def floor? (partial = \.))

(defn found? [seats coord]
  (let [p (get-in seats coord)]
    (or
     (nil? p)
     (seat-empty? p)
     (occupied? p))))

(defn find-first [seats coord dir]
  (->> coord
       (iterate #(map + dir %))
       (drop 1)
       (drop-while (complement (partial found? seats)))
       first
       (get-in seats)))

(defn first-see [coord seats]
  (->> around
       (keep (partial find-first seats coord))
       (filter occupied?)
       count))

(defn seating-wide-rule [seat adj]
  (cond
    (and (= \L seat) (zero? adj)) \#
    (and (= \# seat) (< 4 adj)) \L
    :else seat))

(defn seating-wide [seats]
  (reduce
   (fn [result coord]
     (let [adj (first-see coord seats)]
       (update-in result coord seating-wide-rule adj)))
   seats
   (all-seats seats)))

(def seating-area-wide (partial seating-area* seating-wide))

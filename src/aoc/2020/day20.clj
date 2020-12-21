(ns aoc.2020.day20
  (:require [clojure.set :as cst]
            [clojure.tools.trace :as tr]
            [clojure.string :as s]))


(def flip-h reverse)


(defn rotate [tile]
  (->> tile
       (apply map vector)
       (map reverse)))


(defn ch->bin [s]
  (let [s (if (string? s) s (apply str s))
        ->bin {"#" "1" "." "0"}]
    (-> s
        (clojure.string/replace #"[#\.]" ->bin)
        (Integer/parseInt 2))))


(defn tile->nbrs [tile]
  (let [rot (rotate tile)
        [a d c b] (->> tile
                       (iterate rotate)
                       (take 4)
                       (map first))
        [r1 r2 r3 r4] (map reverse [a b c d])]
    (->> [a b r3 r4 c d r2 r1]
         (map ch->bin))))

(def side-nbrs (comp (partial take 4) tile->nbrs))


(defn common-sides? [s1 s2]
  (->> (set s2)
       (clojure.set/intersection s1)
       seq))


(defn corner? [tiles [id sides]]
  (->> (dissoc tiles id)
       (map second)
       (filter (partial common-sides? (set sides)))
       count
       (= 2)))


(defn filter-corners [tiles]
  (filter (partial corner? tiles) tiles))


(defn map-snd [f m]
  (->> m
       (map (fn [[k v]] [k (f v)]))
       (into {})))


(defn find-corners [tiles]
  (->> tiles
       (map-snd tile->nbrs)
       filter-corners
       (map first)
       (apply *)))


(defn permutations [tile]
  (let [rotated (->> tile (iterate rotate) (take 4))]
    (->> rotated
         (map flip-h)
         (concat rotated)
         (map (juxt side-nbrs identity)))))


(defn find-target-side [{tgt-sides :sides} tgt-common]
  "Finds the matching number (side) and use x + 2 mod 4"
  (assert tgt-sides)
  (let [[side nbr] (->> tgt-sides
                        (map-indexed vector)
                        (filter (fn [[i side]] (tgt-common side)))
                        first)]
    (assert side (format "Side not found %s with %s" (vec tgt-sides ) tgt-common) )
    [(mod (+ side 2) 4) nbr]))


(def idx->pos [[1 0] [0 -1] [-1 0] [0 1]])


(defn add-pos [pos idx]
  (map + pos (idx->pos idx)))


(defn matching-side [sides common]
  (->> sides
       (map-indexed vector)
       (filter (fn [[i side]] (common side)))
       first))


(defn match-side? [tgt-idx expected [pside ptile]]
  (= expected (nth pside tgt-idx)))


(defn find-orientation [found src tiles]
  (let [[tgt-id tgt-common] (->> src :common (filter (comp found first)) first)
        tgt (found tgt-id)
        [idx expected] (matching-side (tgt :sides) tgt-common)
        tgt-idx (mod (+ idx 2) 4)]

    (->> src
         :tile
         permutations
         (filter (partial match-side? tgt-idx expected))
         first
         (zipmap [:sides :tile])
         (merge {:pos (add-pos (tgt :pos) tgt-idx)})
         (assoc found (src :id)))))


(defn add-pending [pending {:keys [common] :as tile} found tiles]
  (->> common
       (map (comp tiles first))
       (remove (comp found :id))
       (map (juxt :id identity))
       (into {})
       (merge pending)))


(defn orientation-step [{:keys [pending tiles found] :as state}]
  (let [[id tile] (first pending)]
    (-> state
        (update :pending dissoc id)
        (update :pending add-pending tile found tiles)
        (update :found find-orientation tile tiles))))


(defn add-sides [tiles]
  (->> tiles
       (map (fn [[k tile]]
              [k {:sides (tile->nbrs tile)
                  :id k
                  :tile tile}]))
       (into {})))

(defn common-sides-tiles [{:keys [id sides]} tiles]
  "Tiles that have sides in common"
  (->> (dissoc tiles id)
       (map (fn [[k {tgt-sides :sides}]]
              [k (clojure.set/intersection (set sides) (set tgt-sides ))]))
       (filter (comp seq second))
       (into {})))

(defn add-common [tiles]
  (->> tiles
       (map-snd (fn [tile]
                  (assoc tile :common (common-sides-tiles tile tiles))))))


(defn empty-puzzle [total]
  (let [width (Math/sqrt total)]
    (vec
     (repeat width
             (vec (repeat width 0))))))


(defn tl-corner? [target [[_ rt btm]]]
  (-> target
      (clojure.set/intersection #{rt btm})
      count
      (= 2)))


(defn tl-corner [{:keys [tile common] :as tl}]
  (let [target (apply clojure.set/union (vals common))]
    (->> tile
         permutations
         (filter (partial tl-corner? target))
         first
         (zipmap [:sides :tile])
         (merge tl))))


(defn init-state
  ([source]
   (let [[corner-id] (->> source
                          (map-snd tile->nbrs)
                          filter-corners
                          first)]
     (init-state source corner-id)))

  ([source corner-id]
   (let [tiles (->> source add-sides add-common)
         found (-> tiles
                   (select-keys [corner-id])
                   (update corner-id tl-corner)
                   (assoc-in [corner-id :pos] [0 0]))]
     {:corner corner-id
      :puzzle (-> source
                  count
                  empty-puzzle
                  (assoc-in [0 0] corner-id))
      :pending (add-pending {} (tiles corner-id) found tiles)
      :found found
      :tiles tiles})))

(defn build-row [row]
  (->> row
       (apply map concat)
       (map (partial apply str))))

(defn ->str-tile [tile]
  (->> tile
       (map (partial apply str))))

(defn remove-borders [tile]
  (->> tile
       (drop 1)
       (take (- (count tile) 2))
       (map #(subs (apply str %) 1 9))))


(defn build-map [found]
  (->> found
       (reduce
        (fn [puzzle [id {:keys [pos tile]}]]
          (->> tile
               remove-borders
               (assoc-in puzzle pos)))
        (empty-puzzle (count found)))
       (mapcat build-row)))


(def monster-ptrn
  ["                  # "
   "#    ##    ##    ###"
   " #  #  #  #  #  #   "])


(def monster-width (count (first monster-ptrn)))


(defn ->ptrn [s]
  (re-pattern (clojure.string/replace s " " ".")) )


(defn monster-start [[i s]]
  (let [middle (->ptrn (second monster-ptrn))]
    (loop [m (re-matcher middle s) found []]
      (if-not (.find m)
        found
        (recur m (conj found [i (.start m) (subs s (.start m) (+ (.start m) monster-width))]))))))


(defn monster-row [image row col]
  (subs (nth image row) col (+ col monster-width)))


(defn before-monster? [image row col]
  "                  # "
  (-> (monster-row image (dec row) col)
      (nth (- monster-width 2))
      (= \#)))


(def after-ptrn #".#..#..#..#..#..#...")


(defn after-monster? [image row col]
  " #  #  #  #  #  #   "
  (->> (monster-row image (inc row) col)
       (re-find (->ptrn (last monster-ptrn)))))


(defn full-monster? [image [row col]]
  (and
   (< 0 row (dec (count image)))
   (before-monster? image row col)
   (after-monster? image row col)))


(defn find-monsters [image]
  "                  #
   #    ##    ##    ###
    #  #  #  #  #  #   "
  (->> image
       (map-indexed vector)
       (mapcat monster-start)
       (filter seq)
       (filter (partial full-monster? image))
       seq
       (#(when % [image %]))))


(defn mark-line [image row col src]
  (->> src
       (map-indexed vector)
       (filter (comp (partial = \#) second))
       (map first)
       (reduce #(assoc-in %1 [row (+ col %2)] \O) image)))


(defn mark [image [row col :as munstr]]
  (-> image
      vec
      (mark-line (dec row) col (first monster-ptrn))
      (mark-line row col (second monster-ptrn))
      (mark-line (inc row) col (nth monster-ptrn 2))))


(defn mark-monsters [[image monsters]]
  (let [image (map vec image)]
    (map (partial apply str)
         (reduce mark image monsters))))


(defn build-full-map [info]
  (->> info
       (iterate orientation-step)
       (drop-while (comp seq :pending))
       first
       :found
       build-map))


(defn find-map-with-monsters [m]
  (->> m
       permutations
       (map (comp (partial map (partial apply str)) second))
       (keep find-monsters)
       first))


(defn count-rough-water [marked-map]
  (->> marked-map
       (map (comp count (partial filter (partial = \#))))
       (apply +)))


(defn water-roughness* [info]
  (->> info
       build-full-map
       find-map-with-monsters
       mark-monsters
       count-rough-water))


(defn water-roughness [tiles]
  (->> tiles
       init-state
       water-roughness*))



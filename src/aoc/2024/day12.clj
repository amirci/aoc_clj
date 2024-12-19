(ns aoc.2024.day12
  (:require [clojure.set :as cset]))


(def all-dir (for [x [-1 0 1] y [-1 0 1]
                        :when (not= (abs x) (abs y))] [x y]))

(defn- perimeter? [possible] (not= 4 (count possible)))

(defn adjacent [pos] (map #(mapv + pos %) all-dir))

(defn- same-target? [region-map target pos] (= target (region-map pos)))

(defn region-data [pos letter] {:area #{pos} :start pos :perimeter #{} :per 0 :letter letter})

(defn find-region
  ([region-map pos] (find-region region-map (region-map pos) (region-data pos (region-map pos)) pos))
  ([region-map target {:keys [area perimeter] :as region} pos]
   (let [possible (->> pos adjacent (filter (partial same-target? region-map target)))
         region (cond-> region
                  :always (update :area conj pos)
                  (perimeter? possible) (cond->
                                         :always (update :perimeter conj pos)
                                         (not (perimeter pos)) (update :per + (- 4 (count possible)))))]
     (reduce (fn [region new-pos] (find-region region-map target region new-pos))
             region
             (filter (complement area) possible)))))


(defn- add-not-visited-region [region-map {:keys [visited] :as acc} pos]
  (if (not (visited pos))
    (let [{:keys [area] :as new-region} (find-region region-map pos)]
      (-> acc
          (update :regions conj new-region)
          (update :visited cset/union area)))
    acc))


(defn find-regions [region-map]
  (->> region-map
       keys
       (reduce (partial add-not-visited-region region-map) {:visited #{} :regions #{}})
       :regions))


(defn total-price [input]
  (->> input
       find-regions
       (map #(-> %
                 (update :area count)
                 (dissoc :perimeter)))
       (map (juxt :area :per))
       (map #(apply * %))
       (apply +)))


(defn add-pt [p1 p2]
  (mapv + p1 p2))

(defn mul-pt [p1 p2]
  (mapv * p1 p2))


(defn- expand-pts [pt]
  (let [pt (mul-pt pt [5 5])]
    (for [x [0 5] y [0 5]]
      (add-pt pt [x y]))))


(defn sides [pt]
  (let [pt1 (map #(quot % 5) pt)]
    (->> [[0 0] [-1 0] [-1 -1] [0 -1] [-1 -1] [-1 0] [0 0] [0 -1]]
         (map (partial add-pt pt1))
         (partition 2)
         (map set))))


(defn sides? [area pt]
  (->> pt
       sides
       (some #(cset/subset? % area))))


(defn vertexes [area]
  (->> area
       (mapcat expand-pts)
       frequencies
       (map (fn [[pt n]] (cond
                           (odd? n) 1
                           (and (= 2 n) (not (sides? area pt))) 2
                           :else 0)))
       (apply +)))


(defn total-price-bulk [input]
  (->> input
       find-regions
       (map (fn [{:keys [area]}]
              (->> area
                   vertexes
                   (* (count area)))))
       (apply +)))


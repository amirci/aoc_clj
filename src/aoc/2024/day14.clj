(ns aoc.2024.day14
  (:require
   [clojure.string :as s]
   [clojure.math :as math]
   [clojure.set :as cset]))


(defn multimap [pairs]
  (reduce
   (fn [m [a b]]
     (update m a (fnil conj #{}) b))
   {}
   pairs))


(defn- modulito [[rows cols] [col row]]
  [(mod col cols) (mod row rows)])


(defn- move-to-new-pos [[rows cols] [pos robots]]
  (->> robots
       (map (fn [velocity] (->> (mapv + pos velocity)
                               (modulito [rows cols])
                               (#(vector % velocity)))))))


(defn elapse-second [grid {:keys [iteration] :as robots}]
  (->> (dissoc robots :iteration)
       (mapcat (partial move-to-new-pos grid))
       multimap
       (#(assoc % :iteration ((fnil inc 0) iteration)))))


(defn quarters [rows cols]
  (let [mid-col (quot cols 2)
        mid-row (quot rows 2)]
    (fn [[col row]]
      (cond
        (and (< col mid-col) (< row mid-row)) 1
        (and (< mid-col col) (< row mid-row)) 2
        (and (< col mid-col) (< mid-row row)) 3
        (and (< mid-col col) (< mid-row row)) 4
        :else 0))))


(defn- safety-factor [[rows cols] robots]
  (->> (dissoc robots :iteration)
       (group-by (comp (quarters rows cols) first))
       (#(-> % (dissoc 0)))
       (map (fn [[_ robots]] (->> robots
                                  (map second)
                                  (apply +))))
       (apply *)))


(defn robots-after [grid secs robots]
  (->> robots
       (iterate (partial elapse-second grid))
       (drop secs)
       first
       (#(dissoc % :iteration))
       (map #(update % 1 count))
       (into {})))


(defn safety-factor-after [grid secs robots]
  (->> robots
       (robots-after grid secs)
       (safety-factor grid)))

(defn- ->picture [[rows cols] {:keys [iteration] :as robots}]
  (s/join "\n"
          (for [row (range rows)]
            (apply str
                   (for [col (range cols) :let [robot (robots [col row])]]
                     (if robot \* \.))))))


(defn- mk-tree [[rows cols]]
  (let [mid-col (quot cols 2)
        mid-row (quot rows 2)]
    (->> (for [row (range 2) :let [row (inc row)]]
           [[(- mid-col row) row] [(+ mid-col row) row]])
         (apply concat)
         (apply conj #{[mid-col 0]}))))


(defn- line? [row-pts]
  (->> row-pts
       (map second)
       sort
       (reduce (fn [[max-grp current prev] pt]
                 (if (= ((fnil inc -2) prev) pt)
                   [(max max-grp (inc current)) (inc current) pt]
                   [max-grp 1 pt]))
               [0 0])
       first
       (< 10)))


(defn- has-line-of-robots? [robots]
  (->> (dissoc robots :iteration)
       keys
       (group-by first)
       (map second)
       (some line?)
       ))


(defn iterations-until-tree [grid robots]
  (let [tree (mk-tree grid)]
    (->> robots
         (iterate (partial elapse-second grid))
         (drop 1)
         (drop-while #(and (< (:iteration %) 6600)
                           (not (has-line-of-robots? %)) ))
         first
         ((juxt :iteration (partial ->picture grid))))))

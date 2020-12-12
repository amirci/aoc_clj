(ns aoc.2020.day12)

(def neg (partial * -1))

(defn rotate [[x y] deg]
  (case deg
    90  [y (neg x)]
    180 [(neg x) (neg y)]
    270 [(neg y) x]))

(defn cc-rotate [dir deg]
  (rotate dir (- 360 deg)))

(def dirs {\N [0 1] \S [0 -1] \W [-1 0] \E [1  0]})

(defn parse [inst]
  (-> (re-matches #"(\D)(\d+)" inst)
      rest
      vec
      (update 0 first)
      (update 1 clojure.edn/read-string)))

(defn manhattan [pos]
  (->> pos
       (map #(Math/abs %))
       (apply +)))

(defn scalar [v nbr]
  (map * v (repeat nbr)))

(def sum (partial map +))

(defn run-inst
  [{:keys [dir] :as state} [inst nbr]]
  (let [sc (scalar dir nbr)
        cardinal (scalar (dirs inst) nbr)]
    (case inst
      \F (update state :pos sum sc)
      (\N \S \E \W) (update state :pos sum cardinal)
      \R (update state :dir rotate nbr)
      \L (update state :dir cc-rotate nbr))))


(defn run-waypoint
  [{:keys [dir wp] :as state} [inst nbr]]
  (let [cardinal (scalar (dirs inst) nbr)
        sc (scalar wp nbr)]
    (case inst
      \F (update state :pos sum sc)
      (\N \S \E \W) (update state :wp sum cardinal)
      \R (update state :wp rotate nbr)
      \L (update state :wp cc-rotate nbr))))


(def init-state
  {:dir (dirs \E) :pos [0 0] :wp [10 1]})

(defn distance
  ([input] (distance run-inst input))
  ([fn input]
   (->> input
        (map parse)
        (reduce fn init-state)
        :pos
        manhattan)))


(def dist-wp (partial distance run-waypoint))


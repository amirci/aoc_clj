(ns aoc.2017.day22)


(defn ->nodes [[i line]]
  (->> line
       (map-indexed (partial vector i))
       (filter (comp (partial = \#) last))
       (map butlast)
       (map vec)))


(defn load-nodes [input]
  (->> input
       (map-indexed vector)
       (mapcat ->nodes)
       set))


(def turn-right
  {[ 1  0] [ 0 -1]
   [-1  0] [ 0  1]
   [ 0 -1] [-1  0]
   [ 0  1] [ 1  0]})


(defn turn-left [dir]
  (->> dir
       turn-right
       (map (partial * -1))))


(defn turn [dir infected?]
  (if infected?
    (turn-right dir)
    (turn-left dir)))


(defn infect [nodes pos infected?]
  (if infected?
    (disj nodes pos)
    (conj nodes pos)))


(defn move-fwd [pos dir]
  (map + pos dir))


(defn burst
  ([state] (burst turn infect seq state))
  ([turn infect infected? {:keys [pos dir nodes] :as state}]
   (let [status (nodes pos)
         new-dir (turn dir status)
         nodes (infect nodes pos status)]
     (-> state
         (assoc :dir new-dir)
         (assoc :nodes nodes)
         (update :pos move-fwd new-dir)
         (update :infected + (if (infected? (nodes pos)) 1 0))))))


(defn bursts
  ([input rounds] (bursts load-nodes burst input rounds))
  ([load burst input rounds]
   (let [nodes (load input)
         mid (quot (count input) 2)]
     (->> {:pos [mid mid] :dir [-1 0] :nodes nodes :infected 0}
          (iterate burst)
          (drop rounds)
          first))))


(defn load-nodes-map [input]
  (->> input
       load-nodes
       (map #(vector % \#))
       (into {})))


(def weakened? (partial = \W))
(defn clean? [s] (= \. (or s \.)))
(def flagged? (partial = \F))
(def infected? (partial = \#))

(def reverse-dir (comp turn-right turn-right))

(defn turn-adv
  "If it is clean, it turns left.
   If it is weakened, it does not turn, and will continue moving in the same direction.
   If it is infected, it turns right.
   If it is flagged, it reverses direction, and will go back the way it came."
  [dir status]
  (cond-> dir
    (clean? status) turn-left
    (infected? status) turn-right
    (flagged? status) reverse-dir))


(def advanced {\. \W \W \# \# \F \F \.})


(defn infect-adv [nodes pos status]
  (let [new-status (advanced (or status \.))]
    (if (= \. new-status)
      (dissoc nodes pos)
      (assoc nodes pos new-status))))


(def burst-advanced
  (partial burst turn-adv infect-adv infected?))


(def bursts-advanced
  (partial bursts load-nodes-map burst-advanced))

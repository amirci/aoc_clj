(ns aoc.2017.day19)


(def ahead? (partial = \|))


(defn find-start [line]
  (->> line
       (map-indexed vector)
       (filter (comp ahead? second))
       ffirst
       (vector 0)))


(def dirs
  {:up [-1 0]
   :down [1 0]
   :right [0 1]
   :left [0 -1]})


(defn collect [letters tgt]
  (cond-> letters
    (Character/isLetter tgt) (conj tgt)))


(defn move-to [dir pos]
  (map + pos (dirs dir)))


(def not-routed \space)


(def routed? (partial not= not-routed))


(defn choose [[d1 d2] pos routing]
  (if (routed? (get-in routing (move-to d1 pos)))
    d1
    d2))


(defn change-dir [dir tgt pos routing]
  (if (= \+ tgt)
    (let [[x y] (dirs dir)]
      (if (zero? x)
        (choose [:up :down] pos routing)
        (choose [:left :right] pos routing)))
    dir))


(defn follow [{:keys [pos dir routing] :as state}]
  (let [new-pos (move-to dir pos)
        tgt (get-in routing new-pos)]
    (-> state
        (update :steps inc)
        (assoc :pos new-pos)
        (update :dir change-dir tgt new-pos routing)
        (update :letters collect tgt))))


(defn valid-move? [{:keys [pos dir routing]}]
  (as-> (move-to dir pos) p
    (get-in routing p not-routed)
    (routed? p)))


(defn follow-routing* [lines]
  (let [start (find-start (first lines))
        lines (mapv vec lines)]
    (->> {:routing lines :pos start :dir :down :letters [] :steps 1}
         (iterate follow)
         (drop-while valid-move?)
         first)))


(defn follow-routing [lines]
  (->> lines
       follow-routing*
       :letters
       (apply str)))


(defn count-routing-steps [lines]
  (->> lines
       follow-routing*
       :steps))

(ns aoc.2022.day2)

(declare expected-move)


(def oponent-code {\A :rock \B :paper \C :scissors })
(def my-code {\X :rock \Y :paper \Z :scissors })

(def rules {:paper :rock
            :scissors :paper
            :rock :scissors})

(def points {:rock 1 :paper 2 :scissors 3})

(defn- row-points
  [oponent-play my-play]
  (+
     (points my-play)
     (cond
       (= oponent-play my-play) 3
       (= (rules my-play) oponent-play) 6
       :else 0)))

(defn row-score [[oponent mine]]
  (let [oponent-play (oponent-code oponent)
        my-play (my-code mine)]
    (row-points oponent-play my-play)))


(defn total-score
  [strategy]
  (->> strategy
       (map row-score)
       (apply +)))

(def lose-to rules)

(def win-to
  (clojure.set/map-invert rules))

(def expected-fn
  {\X lose-to
   \Y identity
   \Z win-to})

(defn expected-move [[oponent target]]
  (let [oponent-play (oponent-code oponent)
        f (expected-fn target)]
    [oponent-play (f oponent-play)]))

(defn total-score-2
  [strategy]
  (->> strategy
       (map expected-move)
       (map (partial apply row-points))
       (apply +)))




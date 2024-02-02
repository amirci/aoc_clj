(ns aoc.2023.day21)

(defn load-garden [spec]
  (->> (for [x (range 0 (count spec))
             y (range 0 (count (first spec)))
             :let [c (get-in spec [x y])]
             :when (not= \. c)]
         [c [x y]])
       (group-by first)
       (map (comp set (partial map second) second))
       (zipmap [:rocks :pending])))

(defn only-xy? [x y]
  (= 1 (+ (Math/abs x) (Math/abs y))))

(defn- neighbours [rocks [a b]]
  (for [x [-1 0 1] y [-1 0 1]
        :let [pos [(+ a x) (+ b y)]]
        :when (and (only-xy? x y) (not (rocks pos)))]
    pos))


(defn- reach-out [{:keys [rocks pending]}]
  (->> pending
       (reduce (fn [new-pending pos]
                 (->> pos
                      (neighbours rocks)
                      )
                 )
               #{}))
  )

(defn reach-after-steps [n spec]
  (->> spec
       load-garden
       (iterate reach-out)
       (drop n)
       first
       )
  )

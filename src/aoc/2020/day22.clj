(ns aoc.2020.day22)


(defn score [[p1 p2]]
  (let [winner (if (seq p1) p1 p2)]
    (->> winner
         (map * (range (count winner) 0 -1))
         (apply +))))

(defn round [[[p1 & rst1] [p2 & rst2]]]
  (if (< p1 p2)
    [(vec rst1) (conj (vec rst2) p2 p1)]
    [(conj (vec rst1) p1 p2) (vec rst2)]))

(def finished? (partial some empty?))

(defn space-cards [game]
  (->> game
       (iterate round)
       (drop-while (complement finished?))
       first
       score))

(defn subgame? [[[p1 & rst1] [p2 & rst2]]]
  (and
   (<= p1 (count rst1))
   (<= p2 (count rst2))))

(defn subgame [{[[p1 & rst1] [p2 & rst2]] :game}]
  {:game [(take p1 rst1) (take p2 rst2)]
   :history #{}})

(defn start-subgame [{:keys [current] :as state}]
  (-> state
      (update :pending conj current)
      (update :current subgame)))

(defn round-history
  ([current] (round-history current round))
  ([current round]
   (-> current
       (update :game round)
       (update :history conj (current :game)))))

(defn round-subgame
  [[sg1 sg2 :as subgame] [[p1 & rst1] [p2 & rst2]]]
  (if (empty? sg1)
    [(vec rst1) (conj (vec rst2) p2 p1)]
    [(conj (vec rst1) p1 p2) (vec rst2)]))

(defn finish-subgame [{:keys [pending current] :as state }]
  {:pre [(seq pending)]}
  (-> state
      (update :pending pop)
      (assoc :current (peek pending))
      (update :current round-history (partial round-subgame (current :game)))))

(def p1-wins [[1] []])

(defn recursive-round [{{:keys [game history]} :current :as state}]
  (cond
    (history game) (assoc-in state [:current :game] p1-wins)
    (finished? game) (finish-subgame state)
    (subgame? game) (start-subgame state)
    :regular-game (update state :current round-history)))

(defn finished-recursive? [{:keys [pending current]}]
  (and (empty? pending) (finished? (current :game))))

(defn recursive-space-cards [game]
  (->> game
       (assoc {:history #{}} :game)
       (assoc {:pending []} :current)
       (iterate recursive-round)
       (drop-while (complement finished-recursive?))
       first
       :current
       :game
       score
       ))

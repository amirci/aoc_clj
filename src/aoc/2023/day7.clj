(ns aoc.2023.day7)

(def face-value {\A 14
                 \K 13
                 \Q 12
                 \J 11
                 \T 10})

(def face-value-j
  (assoc face-value \J 1))

(defn card-value
  "A, K, Q, J, T, 9, 8, 7, 6, 5, 4, 3, or 2"
  [card]
  (or (face-value card) (Character/digit card 10)))

(defn card-value-j
  "A, K, Q, T, 9, 8, 7, 6, 5, 4, 3, 2, J"
  [card]
  (or (face-value-j card) (Character/digit card 10)))


(defn cards-values
  ([cards] (cards-values card-value cards))
  ([->value cards] (reduce (fn [acc c] (+ (* acc 16) (->value c))) 1 cards)))

(defn rank
  "Five of a kind, where all five cards have the same label: AAAAA
   Four of a kind, where four cards have the same label and one card has a different label: AA8AA
   Full house, where three cards have the same label, and the remaining two cards share a different label: 23332
   Three of a kind, where three cards have the same label, and the remaining two cards are each different from any other card in the hand: TTT98
   Two pair, where two cards share one label, two other cards share a second label, and the remaining card has a third label: 23432
   One pair, where two cards share one label, and the other three cards have a different label from the pair and each other: A23A4
   High card, where all cards' labels are distinct: 23456"
  [{fours 4 fives 5 threes 3 twos 2} hand]
  (-> (cond
        ; five of a kind
        fives  7
        ; four of a kind
        fours  6
        ; full house
        (and threes twos) 5
        ; three of a kind
        threes 4
        ; two pairs
        (= 2 (count twos) ) 3
        ; one pair
        twos 2
        :else 1)
      (vector
       (+ (cards-values hand)))))

(defn rank-j-rule [{:keys [jc] fours 4 fives 5 threes 3 twos 2 ones 1} hand]
  (let [j-1 (= 1 jc) j-2 (= 2 jc) j-4 (= 4 jc) j-3 (= 3 jc)]
    (-> (cond
          ; five of a kind
          (or fives
              j-4
              (and fours j-1)
              (and threes j-2)
              (and twos j-3))  6
          ; four of a kind
          (or fours
              j-3
              (and threes j-1)
              (and twos j-2 (not= [\J] twos)))  5
          ; full house
          (or (and threes twos)
              (and threes j-1)
              (and (= 2 (count twos)) j-1)) 4
          ; three of a kind
          (or threes
              (and j-2 (= 3 (count ones)))
              (and twos j-1)) 3
          ; two pairs
          (or (= 2 (count twos))) 2
          ; one pair
          (or twos jc) 1
          :else 0)
        (vector (+ (cards-values card-value-j hand))))))

(defn- invert-to-mmap [freq]
  (->> freq
       (reduce (fn [m [k v]] (update m v (fnil conj []) k)) {})
       (#(assoc % :jc (freq \J)) )))


(defn hand-with-bid
  ([game] (hand-with-bid rank game))
  ([rank game]
   (->> game
        (re-matches #"([2-9AJQTK]{5}) (\d+)")
        rest
        ((fn [[hand bid]]
           (let [freq (invert-to-mmap (frequencies hand))]
             (vector (read-string bid)
                     (rank freq hand)
                     freq
                     hand)))))))


(defn winnings [game]
  (->> game
       (map hand-with-bid)
       (sort-by second)
       (map-indexed (fn [i [bid]] (* (inc i) bid)))
       (apply +)))

(defn winnings-j-rule [game]
  (->> game
       (map (partial hand-with-bid rank-j-rule))
       (sort-by second)
       (map-indexed (fn [i [bid]] (* (inc i) bid)))
       (apply +)))

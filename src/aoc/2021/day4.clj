(ns aoc.2021.day4
  (:require [clojure.set]))


(defn parse-nbrs
  [line]
  (if (seq line)
    (read-string (str "[" line "]"))
    ""))


(defn parse-boards
  [input]
  (let [boards (->> input
                  (partition 6)
                  (map rest))]
    (->> boards
         (map #(concat % (apply map vector %)))
         (map (partial map set)))))


(defn parse-bingo
  [[nbrs & boards]]
   [nbrs (parse-boards boards)])


(defn parse-input
  [lines]
  (->> lines
       (map parse-nbrs)
       parse-bingo))


(defn mark-nbr
  [nbr board]
  (map #(disj % nbr) board))


(def winner? (comp some? (partial some empty?)))


(defn bingo-round
  [nbr boards]
  (->> boards
       (map (partial mark-nbr nbr))
       (group-by winner?)
       (#(clojure.set/rename-keys % {true :winners false :losers}))))


(defn calc-score
  [[board nbr]]
  (->> board
       (apply concat)
       set
       (apply +)
       (* nbr)))


(defn play-bingo
  [[nbrs boards]]
  (->> nbrs
       (reduce
        (fn [boards nbr]
          (let [{:keys [winners losers]} (bingo-round nbr boards)]
            (assert (>= 1 (count winners)))
            (if (seq winners)
              (reduced [(first winners) nbr])
              losers)))
        boards)
       calc-score))


(defn play-bingo-squid
  [[nbrs boards]]
  (->> nbrs
       (reduce
        (fn [boards nbr]
          (let [{:keys [winners losers]} (bingo-round nbr boards)
                [winner & rst] winners]
            (assert (or (seq losers) (and winner (not rst))))
            (if (empty? losers)
              (reduced [winner nbr])
              losers)))
        boards)
       calc-score))

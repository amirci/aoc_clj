(ns aoc.2024.day6
  (:require [clojure.set :as cst]))

(defn parse-floor [input]
  (->> (for [x (range (count input))
             y (range (count (first input)))]
         [x y])
       (reduce
        (fn [m pos]
          (condp = (get-in input pos)
            \^ (assoc m :start pos)
            \# (update m :obstacles conj pos)
            m))
        {:visited #{}
         :obstacles #{}
         :index 0
         :dir [-1 0]
         :rows (count input)
         :cols (count (first input))})))


(defn- add-point [[a b] [c d]]
  [(+ a c) (+ b d)])


(defn- outside? [{:keys [rows cols]} [x y :as new-pos]]
  (or (some neg? new-pos)
      (<= rows x)
      (<= cols y)))


(defn rotate-90 [[x y]] [y (* -1 x)])


(defn guard-step [{:keys [current dir visited obstacles] :as m}]
  (assert current "The current pos cannot be nil")
  (assert (not (obstacles current)))
  (let [new-pos (add-point current dir)]
    (cond
      (outside? m new-pos) (-> m
                               (dissoc :current)
                               (update :visited conj [current dir]))
      (visited [new-pos dir]) (assoc m :loop new-pos :current nil)
      (obstacles new-pos) (update m :dir rotate-90)
      :else (-> m
                (update :index inc)
                (assoc :current new-pos)
                (update :visited conj [current dir])))))


(defn find-way-out*
  ([floor-map]
   (->> (assoc floor-map :current (:start floor-map))
        (iterate guard-step)
        (drop-while :current)
        first)))


(defn find-way-out
  ([floor-map]
  (->> floor-map
       parse-floor
       find-way-out*)))


(defn guard-steps [floor-map]
  (->> floor-map
       find-way-out
       :visited
       (map first)
       set
       count))


(defn visited->obstacles [{:keys [start visited] :as m}]
  (->> visited
       (map first)
       set
       (#(disj % start))
       (map #(-> m
                 (assoc :visited #{} :index 0 :dir [-1 0])
                 (update :obstacles conj %)))))


(defn guard-loops [floor-map]
  (->> floor-map
       find-way-out
       (visited->obstacles)
       (map find-way-out*)
       (filter :loop)
       count))

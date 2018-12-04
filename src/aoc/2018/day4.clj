(ns aoc.2018.day4
  (:require 
    [clj-time.core :as tm]
    [clj-time.format :as tf]
    [clojure.string :as str]))

(def custom (tf/formatter "yyyy-MM-dd HH:mm"))

(defn instruction [v & args] (nth v 2))

(defmulti parse-line instruction)

(defmethod parse-line "Guard"
  [[dt time _ nbr] [m last-guard]]
  [m nbr])

(defmethod parse-line "falls"
  [[dt tm] [m last-guard]]
  [m 
   last-guard 
   (tf/parse custom (str dt " " tm))])

(defmethod parse-line "wakes"
  [[dt-to tm-to] [m last-guard from]]
  (let [times (get m last-guard [])
        to (tf/parse custom (str dt-to " " tm-to))
        m (assoc m last-guard (conj times (tm/interval from to)))]
    [m last-guard]))

(defn parse-guards-sleep
  [state log-line]
  (-> log-line
      (str/replace #"[\[\]\#]" "")
      (str/split #" ")
      (parse-line state)))

(defn calc-total-sleep-minutes
  [log]
  (reduce-kv 
    (fn [m k vs]
      (assoc m 
             k
             (apply +
                    (map tm/in-minutes vs))))
    {}
    log))

(defn find-guard-most-asleep
  [log]
  (->> log
       calc-total-sleep-minutes
       (apply max-key val)))

(defn calc-minutes
  [interval]
  (let [start (.withTime (tm/start interval) 0 0 0 0)]
    (->> (for [m (range 60)
               :let [q (tm/plus start
                                (tm/minutes m))]]
           [m (if (tm/within? interval q) 1 0)])
         (into {}))))

(defn part-a
  [log]
  (let [log (->> log
                 (reduce parse-guards-sleep [{}])
                 first)
        guard (first (find-guard-most-asleep log))
        intervals (log guard)
        mm (->> intervals
                (map calc-minutes)
                (apply merge-with +)
                (apply max-key val)
                first)]
    (* (read-string guard) mm)))


(ns aoc.2023.day20
  (:require [blancas.kern.core :as kc :refer [<$> <*> >> <|>]]
            [blancas.kern.lexer.basic :as lex :refer [token sym comma-sep1 identifier]]))


(def arrow-p (token "->"))


(def module-id-p identifier)


(def module-id-list-p (comma-sep1 module-id-p))


(def arrow-list-p (>> arrow-p module-id-list-p))


(defn create-signals [id signal ids]
  (map (partial vector id signal) ids))


(defn broadcast-signal [id ids _]
  (fn [state signal _]
    (->> ids
         (create-signals id signal)
         (vector state))))


(def broadcaster-p
  (<$> (fn [[id ids]] [id broadcast-signal ids])
       (<*> module-id-p arrow-list-p)))


(def high? (partial = :high))


(def flip-state {:on :off :off :on})


(def ->signal {:on :high :off :low})



(defn flip-low-signal [id ids _]
  (fn [state signal _]
    (let [on? (state id :off)
          flipped (flip-state on?)]
      (if (high? signal)
        [state []]
        (->> ids
             (create-signals id (->signal flipped))
             (vector (assoc state id flipped)))))))


(def flip-flop-p
  (<$> (fn [[id ids]] [id flip-low-signal ids])
       (<*> (>> (sym \%) identifier) arrow-list-p)))



(defn- conj-signal [id ids inputs]
  (fn [state signal source]
    (let [memory (-> id (state {}) (assoc source signal))
          all-inputs (map #(get memory % :low) inputs)
          high? (every? #{:high} all-inputs)]
      (->> ids
           (create-signals id (if high? :low :high))
           (vector (assoc state id memory))))))


(def conjunction-p
  (<$> (fn [[id ids]] [id conj-signal ids])
       (<*> (>> (sym \&) identifier) arrow-list-p)))


(def module-p
  (<|> broadcaster-p flip-flop-p conjunction-p))


(defn- parse-module [line]
  (kc/value module-p line))


(defn- calculate-inputs [parsed-config]
  (->> parsed-config
       (reduce (fn [acc [id _ ids]]
                 (reduce (fn [acc target]
                           (update acc target conj id))
                         acc
                         ids))
               {})
       (vector parsed-config)))


(defn- ->modules [[parsed-config deps]]
  (->> parsed-config
       (map (fn [[id f ids]] [id (f id ids (deps id))]))))


(defn parse-config [config]
  (->> config
       (map parse-module)
       (calculate-inputs)
       ->modules
       (into {})))


(defn- send-signals
  [modules {:keys [pending state] :as push-state}]
  (let [[source pulse target] (first pending)
        module (modules target)
        [new-state new-pending] (if module
                                  (module state pulse source)
                                  [state []])]
    #_(when (and (#{"vr" "nl" "gt" "lr"} target)
               (= :low pulse))
      (println "Low on " target "from" source "on" (:pushes state))
      )
    (-> push-state
        (update :pending rest)
        (update :pending concat new-pending)
        (assoc :state new-state)
        (update-in [:state pulse] (fnil inc 0)))))


(defn push-button [modules state]
  (->> {:pending [["button" :low "broadcaster"]]
        :state state}
       (iterate (partial send-signals modules))
       (drop-while (comp seq :pending))
       first
       :state
       (#(update % :pushes (fnil inc 0)))))


(defn mul-low-high [config n]
  (let [modules (parse-config config)]
    (->> {}
         (iterate (partial push-button modules))
         (drop n)
         first
         ((juxt :high :low))
         (apply *))))



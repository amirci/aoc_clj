(ns aoc.2020.day8
  (:require [clojure.string :as s]
            [clojure.edn :as edn]))


(defn parse-inst
  [s]
  (-> s
      (clojure.string/split #" ")
      vec
      (update 0 keyword)
      (update 1 edn/read-string)))

(defn inst [{:keys [ptr program]}]
  (-> program
      (get ptr)
      first))

(defmulti run-inst inst)

(defmethod run-inst :nop
  [{:keys [ptr] :as state}]
  (-> state
      (update :visited conj ptr)
      (update :ptr inc)))


(defmethod run-inst :acc
  [{:keys [ptr acc program] :as state}]
  (let [[_ v] (get program ptr)]
    (-> state
        (update :acc + v)
        (update :visited conj ptr)
        (update :ptr inc))))


(defmethod run-inst :jmp
  [{:keys [ptr program] :as state}]
  (let [[_ v] (get program ptr)]
    (-> state
        (update :visited conj ptr)
        (update :ptr + v))))

(defn loop?
  [{:keys [ptr visited]}]
  (visited ptr))

(defn parse [inst]
  (->> inst
       (map parse-inst)
       vec))

(defn run-until-twice [inst]
  (->> inst
       parse
       (assoc {:ptr 0 :acc 0 :visited #{}} :program)
       (iterate run-inst)
       (drop-while (complement loop?))
       first
       :acc))

(defn swap-op [program [idx op]]
  (assoc-in program
            [idx 0]
            (if (= :jmp op) :nop :jmp)))

(defn finished? [{:keys [ptr program] :as state}]
  (or
    (loop? state)
    (= (count program) ptr)))

(defn run-until-finished [program]
  (->> program
       (assoc {:ptr 0 :acc 0 :visited #{}} :program)
       (iterate run-inst)
       (drop-while (complement finished?))
       first))

(defn corrupt [inst]
  (let [program (parse inst)]
    (->> program
         (map-indexed cons)
         (filter (comp (partial not= :acc) second))
         (map (partial swap-op program))
         (map run-until-finished)
         (filter (complement loop?))
         first
         :acc)))


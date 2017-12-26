(ns aoc.2017.day18
  (:require 
    [blancas.kern.core :refer :all]
    [clojure.tools.trace :refer [trace]]
    [blancas.kern.lexer.basic :refer :all]))

(def val-p
  (<$> (comp constantly read-string)
       (<+> (optional (sym \-)) dec-num)))

(def register-p (<$> #(fn [st] (or (get-in st [:regs %]) 0)) letter))

(def reg-or-val-p (<|> register-p val-p))

(defn reg-fn-p [tk] (<*> (token tk) letter space reg-or-val-p))

(defn inc-ptr
  [st]
  (update st :ptr inc))

(defn update-register-with-op
  [op reg val-fn st] 
  (update-in st [:regs reg] #(op (or % 0) (val-fn st))))

(defn reg-update-p
  [tk op]
  (<$> (fn [[_ reg _ val-fn]] (comp inc-ptr (partial update-register-with-op op reg val-fn)))
    (reg-fn-p tk)))

;; set x
(def set-p (reg-update-p "set" (fn [_ b] b)))

;; add a 2
(def add-p (reg-update-p "add" +))

;; mul a a
(def mul-p (reg-update-p "mul" *))

;; mod a 5
(def mod-p (reg-update-p "mod" mod))

;; snd a
(def snd-p
  (<$> (fn [f] (comp inc-ptr #(assoc % :snd (f %))))
    (>> (token "snd") reg-or-val-p)))

(defn recover-last
  [f st]
  (if (not= 0 (f st))
    (assoc st :rcv true)
    st))

;; rcv a
(def rcv-p
  (<$> (fn [f] (comp inc-ptr (partial recover-last f)))
    (>> (token "rcv") reg-or-val-p)))

(defn jump-greater-zero
  [fr fjump st]
  (if (not= 0 (fr st))
    (update st :ptr (partial + (fjump st)))
    (inc-ptr st)))

;; jgz a -1
(def jgz-p
  (<$>
    (fn [[_ fr _ fjump]] (partial jump-greater-zero fr fjump))
    (<*> (token "jgz") reg-or-val-p space reg-or-val-p)))

(def inst-parser (<|> set-p add-p mul-p mod-p snd-p rcv-p jgz-p))

(defn run-one
  [{:keys [inst ptr] :as state}]
  (-> inst
      (nth ptr)
      (apply [state])))

(defn run-program
  [ist]
  (->> ist
       (map (partial value inst-parser))
       (hash-map :ptr 0 :regs {} :snd nil :inst)
       (iterate run-one)))

(defn find-first-rcv
  [inst]
  (->> inst
       run-program
       (drop-while (complement :rcv))
       first
       :snd))

(defn run-both-programs
  [inst]
  )

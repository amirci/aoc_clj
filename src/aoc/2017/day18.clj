(ns aoc.2017.day18
  (:require 
    [blancas.kern.core :refer :all]
    [clojure.tools.trace :refer [trace]]
    [blancas.kern.lexer.basic :refer :all]))


(def val-p 
  (<$> (fn [[s n]] (constantly (* (read-string (str s "1")) n)))
       (<*> (option \+ (sym \-)) dec-num)))

(def register-p (<$> #(fn [st] (get-in st [:regs %])) letter))

(def reg-or-val-p (<|> register-p val-p))

(defn reg-fn-p [tk] (<*> (token tk) letter space reg-or-val-p))

(defn reg-update-p
  [tk f]
  (<$> (fn [[_ r _ v-fn]] (fn [st] (-> st
                                       (update :ptr inc)
                                       (update-in [:regs r] #(f % (v-fn st))))))
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
  (<$> (fn [f] #(assoc % :snd (f %)))
    (>> (token "snd") reg-or-val-p)))

;; rcv a
(def rcv-p
  (<$> (fn [f] #(if (not= 0 (f %)) (assoc % :rcv true)))
    (>> (token "rcv") reg-or-val-p)))

;; jgz a -1
(def jgz-p
  (<$> (fn [_ fr _ fjmp] (fn [st] (if (not= 0 (fr st)) (update st :ptr (partial + (fjmp st))))))
    (<*> (token "jgz") reg-or-val-p space reg-or-val-p )))

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

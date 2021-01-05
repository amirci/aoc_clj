(ns aoc.2017.day23
  (:require
   [blancas.kern.core :as kc :refer [value >> <$> <|> <*>]]
   [blancas.kern.lexer.java-style :as lx]))


"set X Y sets register X to the value of Y.
sub X Y decreases register X by the value of Y.
mul X Y sets register X to the result of multiplying the value contained in register X by the value of Y.
jnz X Y jumps with an offset of the value of Y, but only if the value of X is not zero. (An offset of 2 skips the next instruction, an offset of -1 jumps to the previous instruction, and so on."

(def ref-expr (<|> kc/letter lx/dec-lit))


(def refs-expr
  (<*> ref-expr (kc/skip-ws ref-expr)))


(defn inst-expr [tkn f]
  (<$> (fn [[x y]] (partial f x y))
       (>> (lx/token tkn) refs-expr)))


(defn set-register [x y {:keys [regs] :as state}]
  (-> state
      (update :ptr inc)
      (assoc-in [:regs x] (get regs y y))))


(defn sub-register [x y {:keys [regs ptr] :as state}]
  (-> state
      (update :ptr inc)
      (update-in [:regs x] - (get regs y y))))


(defn mul-register [x y {:keys [regs ptr] :as state}]
  (-> state
      (update :ptr inc)
      (update :mul inc)
      (update-in [:regs x] * (get regs y y))))


(defn jnz-register [x y {:keys [regs] :as state}]
  (let [test (get regs x x)]
    (if (not= 0 test)
      (update state :ptr + (get regs y y))
      (update state :ptr inc))))


(def all-expr
  (->> {"set" set-register
        "sub" sub-register
        "mul" mul-register
        "jnz" jnz-register}
       (map (partial apply inst-expr))
       (apply <|>)))


(defn parse-inst [input]
  (->> input
       (map (partial value all-expr))))


(defn still-running? [{:keys [ptr code]}]
  (< ptr (count code)))


(defn code-step [{:keys [regs input ptr code] :as state}]
  (let [result ((code ptr) state)]
    (println ptr ":" (input ptr) "->" (:regs result))
    result))


(def debug-regs
  (->> "abdefgh"
       (map #(vector % 0))
       (into {})))


(def running-regs (assoc debug-regs \a 1))


(defn run-code
  ([input] (run-code debug-regs input))
  ([regs input]
   (->> input
        parse-inst
        vec
        (assoc {:input input :ptr 0 :mul 0 :regs regs} :code)
        (iterate code-step)
        (drop-while still-running?)
        first)))



(defn divides? [m n] (zero? (rem m n)))

(defn prime? [n]
  (and (< 1 n)
       (not-any? (partial divides? n) (range 2 (Math/sqrt n)))))


(defn program [init]
  (let [b (+ (* init 100) 100000)
        c (+ b 17000)]
    (->> (range b (inc c) 17)
         (filter (complement prime?))
         count)))


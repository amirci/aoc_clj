(ns aoc.2018.day16)


; 3044 line number for instructions

(def register get)
(defn immediate [_ a] a)

(defn calc-op
  [op f regs [a b c :as params]]
  (assoc regs c (op (regs a) (f2 regs b))))

(def addr (partial calc-op + register))
(def addi (partial calc-op + immediate))
(def mulr (partial calc-op * register))
(def muli (partial calc-op * immediate))
(def banr (partial calc-op bit-and register))
(def bani (partial calc-op bit-and immediate))
(def borr (partial calc-op bit-or  register))
(def bori (partial calc-op bit-or  immediate))

(defn setr [regs [a _ c]] (assoc regs c (regs a)))
(defn seti [regs [a _ c]] (assoc regs c a))

(defn bool-op [op? f1 f2 regs [a b c]]
  (->> (if (op? (f1 regs a) (f2 regs b)) 1 0)
       (assoc regs c)))

(defn gtir (partial bool-op > immediate register))
(defn gtri (partial bool-op > register immediate))
(defn gtrr (partial bool-op > register register))
(defn eqir (partial bool-op = immediate register))
(defn eqri (partial bool-op = register immediate))
(defn eqrr (partial bool-op = register register))
 


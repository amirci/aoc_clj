(ns aoc.2018.day16)


; 3044 line number for instructions

(def register nth)
(defn immediate [_ a] a)

(defn calc-op
  [op f regs [a b c :as params]]
  (assoc regs c (op (regs a) (f regs b))))

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

(def gtir (partial bool-op > immediate register))
(def gtri (partial bool-op > register immediate))
(def gtrr (partial bool-op > register register))
(def eqir (partial bool-op = immediate register))
(def eqri (partial bool-op = register immediate))
(def eqrr (partial bool-op = register register))

(defn parse-before-after
  [[before instruction after]]
  (map read-string [before (str "[" instruction"]") after]))


(def all-ops 
  [addi addr muli mulr bani banr bori borr
   setr seti gtir gtri gtrr eqir eqri eqrr])

(defn find-matching-ops 
  [regs [op a b c :as params] actual]
  (->> all-ops
       (filter #(= actual (% regs [a b c])))))

(defn part-a
  [input]
  (->> input
       (map #(apply find-matching-ops %))
       (filter #(<= 3 (count %)))
       count))



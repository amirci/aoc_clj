(ns aoc.2024.day17)


(defn- operand->value
  "[0-3] identity, 4 :A, 5 :B, 6 :C
  Combo operand 7 is reserved and will not appear in valid programs"
  [operand {:keys [A B C]}]
  (assert (<= 0 operand 7) "Operand must be between 0 and 7")
  ([0 1 2 3 A B C nil] operand))


(defn adv
  "The adv instruction (opcode 0) performs division.
  The numerator is the value in the A register.
  The denominator is found by raising 2 to the power of the instruction's combo operand.
  (So, an operand of 2 would divide A by 4 (2^2); an operand of 5 would divide A by 2^B.)
  The result of the division operation is truncated to an integer and then written to the A register. "
  ([state] (adv :A state))
  ([target {:keys [A program ptr] :as state}]
   (let [numerator A
         operand (operand->value (program ptr) state)]
     (-> state
         (update :ptr inc)
         (assoc target (bit-shift-right numerator operand))))))


(defn bxl
  "The bxl instruction (opcode 1) calculates the bitwise XOR of register B
  and the instruction's literal operand, then stores the result in register B."
  [{:keys [B program ptr] :as state}]
  (-> state
      (update :ptr inc)
      (assoc :B (bit-xor B (program ptr)))))


(defn bst
  "The bst instruction (opcode 2) calculates the value of its combo operand modulo 8
  (thereby keeping only its lowest 3 bits), then writes that value to the B register."
  [{:keys [B program ptr] :as state}]
  (-> state
      (update :ptr inc)
      (assoc :B (mod (operand->value (program ptr) state) 8))))


(def not-zero? (complement zero?))


(defn jnz
  "The jnz instruction (opcode 3) does nothing if the A register is 0.
  However, if the A register is not zero, it jumps by setting the instruction
  pointer to the value of its literal operand; if this instruction jumps,
  the instruction pointer is not increased by 2 after this instruction."
  [{:keys [A program ptr] :as state}]
  (cond-> state
    (zero? A) (update :ptr inc)
    (not-zero? A) (assoc :ptr (program ptr))))


(defn bxc
  "The bxc instruction (opcode 4) calculates the bitwise XOR
  of register B and register C, then stores the result in register B.
  (For legacy reasons, this instruction reads an operand but ignores it.) "
  [{:keys [B C] :as state}]
  (-> state
      (update :ptr inc)
      (assoc :B (bit-xor B C))))


(defn out
  "The out instruction (opcode 5) calculates the value of its combo operand modulo 8,
  then outputs that value. (If a program outputs multiple values, they are separated by commas.)"
  [{:keys [program ptr] :as state}]
  (-> state
      (update :ptr inc)
      (update :output conj (mod (operand->value (program ptr) state) 8))))


(def bdv
  "The bdv instruction (opcode 6) works exactly like the adv instruction except
  that the result is stored in the B register.
  (The numerator is still read from the A register.)"
  (partial adv :B))


(def cdv
  "The cdv instruction (opcode 7) works exactly like the adv instruction except that
the result is stored in the C register. (The numerator is still read from the A register.) "
  (partial adv :C))


(def ->instruction [adv bxl bst jnz bxc out bdv cdv])


(defn run-instruction [{:keys [program ptr] :as state}]
  (-> ptr
      program
      ->instruction
      (#(% (-> state
               (update :idx (fnil inc 0))
               (update :ptr inc))))))


(defn not-finished? [{:keys [program ptr]}]
  (< ptr (count program)))


(defn run-til-halts [state]
  (->> (assoc state :ptr 0 :output [])
       (iterate run-instruction)
       (drop-while not-finished?)
       first))


(defn copy-key [m k1 k2]
  (assoc m k2 (m k1)))


(defn find-octal-match [{:keys [program] :as state} [last-n potential]]
  (->> potential
       (mapcat (fn [n]
                 (->> (range 8)
                      (map #(-> state
                                (assoc :ptr 0 :output [] :A (+ (* 8 n) %))
                                (copy-key :A :init)))
                      (map run-til-halts)
                      (filter (comp (partial = (take-last last-n program)) :output))
                      (map :init))))
       (sort)
       (vector (inc last-n))))


(defn find-number-matching-program [state]
  (->> [1 [0]]
       (iterate (partial find-octal-match state))
       (drop 16)
       first
       second
       first))

(ns aoc.2019.day2)

(def halt-code 99)

(defn keep-eval?
  [[ptr code]]
  (-> (nth code ptr)
      (not= halt-code)))

(def next-ptr (partial + 4))

(defn gen-op
  [f memory op1 op2 ptr]
  (let [v1 (nth memory op1)
        v2 (nth memory op2)]
  (assoc memory ptr (f v1 v2))))

(def ->fn {halt-code identity
           1         (partial gen-op +)
           2         (partial gen-op *)})

(defn eval-instruction
  [[op a b c] memory]
  ((->fn op) memory a b c))

(defn run-instruction
  [[ptr code]]
  (let [next (next-ptr ptr)
        ist (subvec code ptr next)]
    [next (eval-instruction ist code)]))

(defn run-program
  [code]
  (->> [0 code]
       (iterate run-instruction)
       (drop-while keep-eval?)
       first
       second
       first))

(defn run-program-with-reset
  [code noun verb]
  (-> code
      (assoc 1 noun)
      (assoc 2 verb)
      run-program))

(defn run-program-1202
  [code]
  (run-program-with-reset code 12 2))

(defn possible-noun-verbs
  [rng]
  (for [i rng j rng] [i j]))

(defn not-matching-target?
  [target code [noun verb]]
  (not= target (run-program-with-reset code noun verb)))

(defn mk-result
  [[n v]]
  (when n
    (+ v (* 100 n))))


(defn find-noun-verb
  [code target]
  (let [nmt? (partial not-matching-target? target code)
        nvs  (possible-noun-verbs (range 100))]
  (->> nvs
       (drop-while nmt?)
       first
       mk-result)))

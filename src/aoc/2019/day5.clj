(ns aoc.2019.day5)

(def halt-code 99)

(def op-len {\1 4 \2 4 \3 2 \4 2 \5 3 \6 3 \7 4 \8 4 \9 2})

(defn next-ptr
  [ptr op]
  (let [delta (get op-len op 0)]
    (+ ptr delta)))

(defn address
  [p]
  (when p
    {:ptr p}))

(def from-address :ptr)
(def address? :ptr)

(def value identity)

(defn read-address
  [m {:keys [ptr] :as address}]
  (get m ptr 0))

(defn read-memory
  [m start end]
  (->> (range start end)
       (map #(read-address m (address %)))))

(defn current-op
  [{:keys [ptr memory]}]
  (let [op (last (str (read-address memory (address ptr))))
        op (read-memory memory ptr (next-ptr ptr op))]
    (first
     (partition 4 4 (repeat nil) op))))

(def address-mode  \0)
(def relative-mode \2)
(def value-mode    \1)

(defn mod-param
  [{:keys [rel-base]} param modifier]
  (condp = modifier
    address-mode  (address param)
    relative-mode (address (+ rel-base param))
    value-mode    (value param)))

(defn parse-modifiers
  [op]
  (let [[op _ m1 m2 m3] (reverse (str op))]
    [op (or m1 \0) (or m2 \0) (or m3 \0)]))

(defn parse-op
  [{:keys [ptr] :as program}]
  (let [[op & params] (current-op program)
        [op & modifiers] (parse-modifiers op)]
    {:op op
     :modifiers modifiers
     :params params
     :actual (map (partial mod-param program) params modifiers)
     :next (next-ptr ptr op)}))

(defn keep-eval?
  [{:keys [ptr memory] :as program}]
  (-> (read-address memory (address ptr))
      (not= halt-code)))

(def non-zero? (complement zero?))

(defn jump-if
  [old-value test? check target]
  (if (test? check)
    target
    old-value))

(def store assoc)

(defn bool->01
  [test? p1 p2]
  (if (test? p1 p2) 1 0))

(defn read-input
  [input program]
  (if (fn? input)
    (input program)
    input))

(defn write-output
  [{:keys [outputs] :as program} v]
  (if (fn? outputs)
    (outputs program v)
    (update program :outputs conj v)))

(defn store-in-memory
  [{:keys [memory] :as program} dst f & params]
  (assert (address? dst) "Destination for should be an address")
  (let [dst (from-address dst)
        v   (apply f params)]
  (update program :memory store dst v)))

(defn actual-value
  [memory param]
  (if (address? param)
    (read-address memory param)
    param))

(defn eval-op 
  [{:keys [op] [a1 a2 dst] :actual} {:keys [memory input] :as program}]
  (let [[v1 v2] (map (partial actual-value memory) [a1 a2])]
    (case op
      \1 (store-in-memory program dst + v1 v2)
      \2 (store-in-memory program dst * v1 v2)
      \3 (store-in-memory program a1 read-input input program)
      \4 (write-output program v1)
      \5 (update program :ptr jump-if non-zero?  v1 v2)
      \6 (update program :ptr jump-if zero?      v1 v2)
      \7 (store-in-memory program dst bool->01 < v1 v2)
      \8 (store-in-memory program dst bool->01 = v1 v2)
      \9 (update program :rel-base + v1))))


(defn update-ptr-if-wasnt-updated
  [op new-ptr old-ptr {:keys [ptr] :as program}]
  (if (= ptr old-ptr)
    (assoc program :ptr new-ptr)
    program))

(defn run-instruction
  [{old-ptr :ptr :as program }]
  (let [{next-ptr :next :as op} (parse-op program)]
    (->> program
         (eval-op op)
         (update-ptr-if-wasnt-updated op next-ptr old-ptr))))

(defn ->instruction
  ([code] (->instruction code 1))
  ([code input] (->instruction code input []))
  ([code input output] 
   {:ptr 0
    :memory (->> code (map-indexed vector) (into {}))
    :input input
    :outputs output
    :rel-base 0}))

(defn run-program*
  [initial]
  (->> initial
       (iterate run-instruction)
       (drop-while keep-eval?)
       first))

(def run-program (comp run-program* ->instruction))

(defn run-program-last-ouput
  ([code] (run-program-last-ouput code 1 []))
  ([code input output]
   (-> code
       (run-program input output)
       :outputs
       last)))

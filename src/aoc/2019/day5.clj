(ns aoc.2019.day5)

(def halt-code 99)

(defn current-op
  [{:keys [runtime ptr]}]
  (subvec (:memory runtime) ptr (+ ptr 4)))

(defn next-ptr
  [ptr op]
  (let [delta (get {\1 4 \2 4 \3 2 \4 2 \5 3 \6 3 \7 4 \8 4} op 0)]
    (+ ptr delta)))

(def address-mode? (partial = \0))

(def address nth)

(defn read-moded-param
  [memory op modifier]
  (cond
    (nil? op) nil
    (address-mode? modifier) (address memory op)
    :else op))

(defn parse-modifiers
  [op]
  (let [s (reverse (seq (str op)))
        op (first s)
        [m1 m2 m3] (drop 2 s)]
    [op (or m1 \0) (or m2 \0) (or m3 \0)]))

(defn parse-op
  [{:keys [ptr runtime] :as program}]
  (let [[op & params] (current-op program)
        [op & modifiers] (parse-modifiers op)
        next (next-ptr ptr op)] [(apply vector op (concat params modifiers)) next]))

(defn keep-eval?
  [{:keys [ptr runtime] :as program}]
  (-> (address (:memory runtime) ptr)
      (not= halt-code)))

(def non-zero? (partial not= 0))

(defn jump-if
  [old-value test? check target]
  (if (test? check)
    target
    old-value))

(defn store-if
  [memory test? dst p1 p2]
  (assoc memory
         dst
         (if (test? p1 p2) 1 0)))

(defn read-input
  [{:keys [input]}]
  (if (fn? input)
    (input)
    input
    ))

(defn eval-op 
  [[op p1 p2 dst m1 m2 m3] {{:keys [input memory] :as runtime} :runtime :as program}]
  (let [v1 #(read-moded-param memory p1 m1)
        v2 #(read-moded-param memory p2 m2)]
    (case op
      \1 (update-in program [:runtime :memory] assoc dst (+ (v1) (v2)))
      \2 (update-in program [:runtime :memory] assoc dst (* (v1) (v2)))
      \3 (update-in program [:runtime :memory] assoc p1 (read-input runtime))
      \4 (update-in program [:runtime :outputs] conj (v1))
      \5 (update-in program [:ptr] jump-if non-zero? (v1) (v2))
      \6 (update-in program [:ptr] jump-if zero? (v1) (v2))
      \7 (update-in program [:runtime :memory] store-if < dst (v1) (v2))
      \8 (update-in program [:runtime :memory] store-if = dst (v1) (v2)))))

(defn update-ptr-if-wasnt-updated
  [op new-ptr old-ptr {:keys [ptr] :as program}]
  (if (= ptr old-ptr)
    (assoc program :ptr new-ptr)
    program))

(defn run-instruction
  [{old-ptr :ptr :as program }]
  (let [[op next-ptr] (parse-op program)]
    (->> program
         (eval-op op)
         (update-ptr-if-wasnt-updated op next-ptr old-ptr))))

(defn ->instruction
  [code input]
  {:ptr 0 :runtime {:memory code :input input :outputs []}})

(defn run-program
  ([code] (run-program code 1))
  ([code input]
  (->> (->instruction code input)
       (iterate run-instruction)
       (drop-while keep-eval?)
       first)))

(defn run-program-last-ouput
  ([code] (run-program-last-ouput code 1))
  ([code input]
   (-> code
       (run-program input)
       :runtime
       :outputs
       last)))

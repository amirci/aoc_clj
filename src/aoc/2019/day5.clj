(ns aoc.2019.day5)

(def halt-code 99)

(defn current-op
  [{:keys [runtime ptr]}]
  (subvec (:memory runtime) ptr (+ ptr 4)))

(defn next-ptr
  [ptr op]
  (let [delta (get {\1 4 \2 4 \3 2 \4 2} op 0)]
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


(defn eval-op 
  [[op p1 p2 dst m1 m2 m3] {:keys [input memory] :as runtime}]
  (let [v1 #(read-moded-param memory p1 m1)
        v2 #(read-moded-param memory p2 m2)]
    (case op
      \1 (update runtime :memory assoc dst (+ (v1) (v2)))
      \2 (update runtime :memory assoc dst (* (v1) (v2)))
      \3 (update runtime :memory assoc p1 input)
      \4 (update runtime :outputs conj (v1)))))

(defn run-instruction
  [program]
  (let [[op next] (parse-op program)]
    (-> program
        (assoc :ptr next)
        (update :runtime (partial eval-op op)))))


(defn run-program
  [code]
  (->> {:ptr 0 :runtime {:memory code :input 1 :outputs []}}
       (iterate run-instruction)
       (drop-while keep-eval?)
       first))

(defn run-program-last-ouput
  [code]
  (-> code
      run-program
      :runtime
      :outputs
      last))

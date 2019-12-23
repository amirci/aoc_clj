(ns aoc.2019.day7
  (:require
   [clojure.math.combinatorics :as combi]
   [clojure.core.async :as async]
   [aoc.2019.day5 :as day5]))

(defn input-queue
  [inputs]
  (let [ch (async/chan)]
    (async/onto-chan ch inputs)
    (fn [program]
      (let [result (async/<!! ch)]
        (when-not result
          (throw (ex-info "CAN'T READ INPUT! Channel closed!" (assoc program :exception :input-required))))
        result))))

(defn run-amplifier
  [program last-output thruster-cfg]
  (let [cfg [thruster-cfg last-output]]
    (day5/run-program program (input-queue cfg))))


(defn thruster-signal
  [program settings]
  (reduce
    (partial run-amplifier program)
    0
    settings))

(def cfg-rng (range 5))

(defn possible-cfgs-seq
  [rng]
  (combi/permutations rng))

(defn max-thruster-signal
  [program]
  (reduce
    (fn [[max-signal max-cfg :as current] cfg]
      (let [signal (thruster-signal program cfg)]
        (if (< max-signal signal)
          [signal cfg]
          current)))
    [0]
    (possible-cfgs-seq (range 5))))

(defn mk-chan
  [n]
  (let [ch (async/chan)]
    (async/onto-chan ch [n])
    ch))

(defn update-state
  [state {{:keys [exception outputs memory]} :runtime :as last-run}]
  (-> state
      (assoc  :exception exception)
      (assoc  :last-output outputs)
      (update :code-states conj last-run)))

(defn run-amplifiers-loop
  [{:keys [code-states last-output] :as last-run}]
  (reduce
    (fn [{:keys [last-output] :as current-run} {:keys [phase] :as program}]
      (update-state current-run
                    (try
                      (let [iq (if phase (cons phase last-output) last-output)
                            program* (-> program
                                         (dissoc :phase)
                                         (dissoc :exception)
                                         (assoc-in [:runtime :outputs] [])
                                         (assoc-in [:runtime :input] (input-queue iq)))]
                        (day5/run-program* program*))
                      (catch clojure.lang.ExceptionInfo exi
                        (ex-data exi)))))
    {:code-states [] :last-output last-output}
    code-states))

(defn ->state
  [code phase]
  (-> (day5/->instruction code 0 [])
      (assoc :phase phase)))

(defn needs-input?
  [{:keys [code-states] :as m}]
  (some :exception code-states))

(defn thruster-signal-in-loop
  [code inputs]

  (->> {:code-states (map ->state (repeat 5 code) inputs) :last-output [0]}
       (iterate run-amplifiers-loop)
       (drop 1) 
       (drop-while needs-input?)
       first
       :code-states
       last
       :runtime
       :outputs
       last))

(defn max-thruster-signal-in-loop
  [program]

  (->> (possible-cfgs-seq (range 5 10))
       (map #(thruster-signal-in-loop program %))
       (apply max)))

(ns aoc.2019.day7
  (:require
   [clojure.math.combinatorics :as combi]
   [clojure.core.async :as async]
   [taoensso.timbre :as timbre]
   [aoc.2019.day5 :as day5]))

(defn read-blocking-fn
  ([ch] (read-blocking-fn ch nil))
  ([ch id]
   (fn [program]
     (when id (timbre/debug "AMP" id "WAITING TO READ input"))
     (let [result (async/<!! ch)]
       (when-not result
         (throw (ex-info (str id ": CAN'T READ INPUT! Channel closed!") (assoc program :exception :input-required))))
       (when id (timbre/debug "AMP" id "READ input" result))
       result))))

(defn input-queue
  [inputs]
  (let [ch (async/chan 2)]
    (async/onto-chan ch inputs)
    (read-blocking-fn ch)))

(defn run-amplifier
  [program last-output thruster-cfg]
  (let [cfg [thruster-cfg last-output]]
    (-> program
        (day5/run-program (input-queue cfg))
        (get-in [:runtime :outputs])
        last)))


(defn thruster-signal
  [program settings]
  (reduce
    (partial run-amplifier program)
    0
    settings))

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

;;; async

(defn write-output-fn
  [ch id]
  (fn [program output]
    (timbre/debug "AMP" id "WRITING output" output)
    (let [new-program (assoc program :last-output output)]
      (async/>!! ch output)
      new-program)))

(defn run-amplifier-async
  [id code ch-input ch-output]

  (let [input (read-blocking-fn ch-input  id)
        output (write-output-fn ch-output id)
        program  (day5/->instruction code input output)
        last-run (day5/run-program* program)]
    (async/close! ch-output)
    (:last-output last-run)))

(defn mk-chan
  [phase]
  (let [ch (async/chan 1)]
    (async/go (async/>! ch phase))
    ch))

(defn thruster-signal-in-loop-async
  [code phases]

  (let [[ea ab bc cd de :as chans] (mapv mk-chan phases)
        pairs (->> (conj chans ea)
                   (partition 2 1)
                   (map vector "ABCDE"))]

    (async/go (async/>! ea 0))

    (deref
      (last
        (for [[id [in out]] pairs]
          (future
            (run-amplifier-async id code in out))))
      10000 0)))

(defn max-thruster-signal-in-loop
  [program]

  (let [f (partial thruster-signal-in-loop-async program)]
    (->> (possible-cfgs-seq (range 5 10))
         (map #(vector % (f %)))
         (apply max-key second))))


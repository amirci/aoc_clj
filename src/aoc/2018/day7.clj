(ns aoc.2018.day7
  (:require 
    [clojure.string :as str]))


(defn parse-line
  [line]
  (let [[_ a _ _ _ _ _ b] (clojure.string/split line #" ")]
    (mapv first [a b])))

(defn build-tree
  [m [a b]]
  (if (m a)
    (update m a conj b)
    (assoc m a (sorted-set b))))

(defn parse
  ([input] (parse input {}))
  ([input base]
   (->> input
        (map parse-line)
        (reduce build-tree base))))

(defn find-complete
  [[completed pending :as state]]
  (let [found (first
                (sort
                  (clojure.set/difference 
                    (set (keys pending))
                    (set (apply concat (vals pending))))))
        pending* (dissoc pending found)
        completed* (conj completed found)
        completed* (if (seq pending*) 
                    completed*
                    (concat completed* (pending found)))]
    [completed* pending*]))

(defn part-a
  [input]
  (->> input
       parse
       (vector []) 
       (iterate find-complete)
       (drop-while (comp seq second))
       first
       first
       (apply str)))

;; PART B
(def abc "HPDTNXYLOCGEQSIMABZKRUWVFJ")

(defn parse-deps
  [input]
  (let [base (->> abc (map #(vector % (sorted-set))) (into {}))]
    (->> input
         (map (comp reverse parse-line))
         (reduce build-tree base))))

(defn work-time
  [l]
  (+ -4 (int l)))

(defn mk-worker [l] [l (work-time l)])

(def duration second)
(def worker-task first)

(defn update-elapsed
  [elapsed [l wt :as worker]]
  [l (- wt elapsed)])

(def finished? (comp zero? duration))

(defn update-finished-deps
  [deps finished]
  (reduce 
    (fn [deps k] (update deps k clojure.set/difference finished))
    deps
    (keys deps)))

(defn tick
  [[total deps pending workers]]
  (let [elapsed  (apply min (map duration workers))
        total    (+ total elapsed)
        workers  (map (partial update-elapsed elapsed) workers)
        finished (set (map worker-task (filter finished? workers)))
        workers* (remove finished? workers)
        deps*    (update-finished-deps deps finished)
        pending* (sort (concat pending (mapcat deps finished)))
        ]
  [total deps* pending* workers*]
  ))

(defn part-b
  [input]
  (let [init (ffirst (find-complete [[] input]))
        deps (parse-deps input)]
    (->> [0 deps (sorted-set) [(mk-worker init)]]
         (iterate tick)
         (take 2))))


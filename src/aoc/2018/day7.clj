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

(defn parse-deps
  [input]
  (let [parsed (map (comp reverse parse-line) input)
        base (->> parsed (map second) set (map #(vector % (sorted-set))) (into {}))]
    (reduce build-tree base parsed)))

(defn work-time
  [l]
  (+ -4 (int l)))

(defn mk-worker [l] [l (work-time l)])

(def duration second)
(def task first)

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

(def max-workers 5)

(defn add-more-work
  [workers available]
  (let [amt (- max-workers (count workers))
        new-workers (->> available (take amt) (map mk-worker))]
    [(concat workers new-workers) (drop amt available)]))

(defn remove-working-tasks
  [deps workers]
  (apply dissoc deps (map task workers)))

(defn tick
  [[total deps pending workers]]
  (let [elapsed  (if (seq workers) (apply min (map duration workers)) 0)
        total    (+ total elapsed)
        workers  (map (partial update-elapsed elapsed) workers)
        finished (set (map task (filter finished? workers)))
        workers* (remove finished? workers)
        deps*    (update-finished-deps deps finished)
        pending* (clojure.set/union
                   pending
                   (apply sorted-set (remove (comp seq deps*) (keys deps*))))
        [workers* pending*] (add-more-work workers* pending*)
        deps* (remove-working-tasks deps* workers*)]
  [total deps* pending* workers*]))

(defn part-b
  [input]
  (let [deps (parse-deps input)]
    (->> [0 deps (sorted-set) []]
         (iterate tick)
         (drop-while (fn [[_ deps _ workers]] (or (seq deps) (seq workers))))
         ffirst)))


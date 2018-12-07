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
    (update m a (comp vec sort conj) b)
    (assoc m a [b])))

(defn parse
  [input]
  (->> input
       (map parse-line)
       (reduce build-tree {})))

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

(defn work-time
  [l]
  (+ -4 (int l)))

(defn mk-worker [l] [l (work-time l)])

(def duration second)
(def worker-task first)

(defn tick
  [total tree pending workers]
  (let [elapsed  (apply min (map duration workers))
        total    (+ total elapsed)
        workers  (map (partial update-elapsed elapsed) workers)
        finished (map worker-task (filter finished? workers))
        workers* (remove finished? workers)
        pending* (sort (concat pending (mapcat tree finished)))
        tree     (apply disj tree finished)]
  
  ))

(defn part-b
  [input]
  (let [init (ffirst (find-complete [[] input]))
        [a & rst :as pending] (construction-order input)
        tree (parse input)]
    (->> [0 tree pending [(mk-worker init) [] [] [] []]]
         (iterate tick)
         (take 2))))


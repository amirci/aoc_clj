(ns aoc.2024.day9)


(defn deque
  ([] (java.util.ArrayDeque.))
  ([coll] (java.util.ArrayDeque. coll)))

(defn dequeLL
  ([] (java.util.LinkedList.))
  ([coll] (java.util.LinkedList. coll)))


(defn deq-get [deq idx] (.get deq idx))

(defn deq-push [deq e] (doto deq (.addLast e)))


(defn deq-pop [deq]
  (assert (not-empty deq))
  [(.removeLast deq) deq])


(defn deq-remove [deq]
  (assert (not-empty deq))
  [(.removeFirst deq) deq])


(def used-blocks? even?)

(def free-blocks? odd?)


(defn- adjust-total [total last-idx nbr-idx n]
  (->> (range last-idx (+ last-idx n))
       (map (partial * (quot nbr-idx 2)))
       (apply + total)))


(defn- move-step [[count total last-idx pending]]
  (assert (not-empty pending))
  (let [[[idx n] butlast] (deq-pop pending)
        smaller (min n count)
        total (adjust-total total last-idx idx smaller)
        last-idx (+ last-idx smaller)]
    (cond
      (<= n count) [(- count n) total last-idx (cond-> butlast
                                                 (not-empty butlast) ((comp second deq-pop)))]
      :else [0 total last-idx (deq-push butlast [idx (- n count)])])))


(defn- move-blocks [count total last-idx pending]
  (->> [count total last-idx pending]
       (iterate move-step)
       (drop-while #(and (not-empty (last %)) (pos? (first %))))
       first
       rest))


(defn- compact-step [[total last-idx pending]]
  (let [[[idx n] rst] (deq-remove pending)]
    (cond
      (used-blocks? idx) [(adjust-total total last-idx idx n)
                          (+ last-idx n)
                          rst]
      :else (move-blocks n total last-idx rst))))


(defn compact-disk [input]
  (let [pending (->> input (map-indexed vector) deque)]
    (->> [0 0 pending]
         (iterate compact-step)
         (drop-while (comp not-empty last))
         ffirst)))


(defn- find-free-space [free-space [used-idx target]]
  (->> free-space
       (take-while (fn [[free-idx]] (< free-idx used-idx)))
       (filter (fn [[_ size]] (<= target size)))
       first))


(defn- update-used [used
                    [source-idx target-size block-idx :as block]
                    [idx]]
  (assert (< idx source-idx))
  (-> used
      (disj block)
      (conj [idx target-size block-idx])))


(defn- update-free [free
                    [_ target-size]
                    [idx size :as found]]

  (cond-> free
    :always (disj found)
    (< target-size size) (conj [(+ idx target-size)
                                (- size target-size)] )))


(defn expand-indexes [coll]
  (->> coll
       (reduce
        (fn [[last-idx idx used free] n]
          (vector (+ last-idx n) (inc idx)
                  (cond-> used
                    (used-blocks? idx) (conj [last-idx n (quot idx 2)]))
                  (cond-> free
                    (free-blocks? idx) (conj [last-idx n]))))
        [0 0 (sorted-set) (sorted-set)])
       (drop 2)))


(defn- expand-checksum [[idx n block-idx]]
  (->> (range idx (+ idx n))
       (map (partial * block-idx))))

(defn compact-disk-full-blocks [input]
  (let [[used free] (->> input expand-indexes)]
    (->> used
         rest
         reverse
         (reduce (fn [[used free] block]
                   (if-let [found (find-free-space free block)]
                     [(update-used used block found)
                      (update-free free block found)]
                     [used free]))
                 [used free])
         first
         (mapcat expand-checksum)
         (apply +))))


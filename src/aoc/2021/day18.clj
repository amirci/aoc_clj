(ns aoc.2021.day18)


(defmulti magnitude vector?)


(defmethod magnitude true [[a b]]
  (+ (* 3 (magnitude a)) (* 2 (magnitude b))))


(defmethod magnitude false [n] n)


(defn leftmost [a b]
  (let [cmp (map compare a b)
        lcmp (apply compare (map count [a b]))
        eq? (every? zero? cmp)]
    (cond
      (= [true 0] [eq? lcmp]) 0
      (= [true 1] [eq? lcmp]) -1
      (= [true -1] [eq? lcmp]) 1
      :else (first (drop-while zero? cmp)))))


(def scalar-pair? (partial every? number?))


(def nested? (comp (partial <= 4) count))


(defn pair? [path pair]
  (and (vector? pair)
       (nested? path)
       (scalar-pair? pair)))


(defn add-new-pair [pairs path pair]
  (cond-> pairs
    (pair? path pair) (conj path)))


(defn add-new-scalar [scalars path e]
  (cond-> scalars
    (<= 10 e) (assoc path e)))


(defn ->nbr
  [nbr]
  (loop [[path & rst] [[0] [1]]
         pairs (sorted-set-by leftmost)
         scalars (sorted-map-by leftmost)]
    (let [e (get-in nbr path)]
      (cond
        (not path) {:pairs pairs :scalars scalars :nbr nbr}
        (number? e) (recur rst
                           pairs
                           (add-new-scalar scalars path e))
        :else (let [lp (conj path 0)
                    rp (conj path 1)]
                (recur (conj rst lp rp)
                       (add-new-pair pairs path e)
                       scalars))))))


(defn butlastv [v]
  (subvec v 0 (dec (count v))))


(defn find-adj
  [branch nbr path]
  (->> path
       (iterate butlastv)
       (drop-while (every-pred seq (comp (partial not= branch) last)))
       first
       ((fn [found]
          (when (seq found)
            (->> (assoc found (dec (count found)) (mod (inc branch) 2))
                 (iterate #(conj % branch))
                 (drop-while (comp vector? (partial get-in nbr)))
                 first))))))


(def find-left (partial find-adj 1))


(def find-right (partial find-adj 0))

(defn add-to-path [nbr path n]
  (cond-> nbr
    path (update-in path + n)))


(defn update-pairs
  [{:keys [nbr] :as state} path]
  (let [parent (butlastv path)
        e (get-in nbr parent)]
    (-> state
        (update :pairs disj path)
        (update :pairs add-new-pair parent e))))


(defn update-scalars
  [{:keys [nbr] :as state} lp rp]
  (let [left (get-in nbr (or lp [-1]) 0)
        right (get-in nbr (or rp [-1]) 0)]
    (assert (number? left) left)
    (assert (number? right) right)
    (-> state
        (update :scalars add-new-scalar lp left)
        (update :scalars add-new-scalar rp right))))


(defn cleanup-scalars
  [{:keys [scalars] :as state} path]
  (->> scalars
       (map first)
       (filter (comp (partial = path) butlastv))
       (update state :scalars (partial apply dissoc))))


(defn explode [{:keys [nbr] :as state} path]
  (let [lp (find-left nbr path)
        rp (find-right nbr path)
        [a b] (get-in nbr path)]
    (-> state
        (update :nbr #(-> %
                          (add-to-path lp a)
                          (add-to-path rp b)
                          (assoc-in path 0)))
        (update-pairs path)
        (update-scalars lp rp)
        (cleanup-scalars path))))


(def next-pair first)


(defn explode-lnp
  [{:keys [pairs] :as nbr}]
  (let [lnp (next-pair pairs)]
    (cond-> [nbr false]
      lnp (-> first (explode lnp) (vector true)))))


(defn new-pair [n]
  (let [q (quot n 2)]
    [q (+ q (mod n 2))]))


(defn split-l10*
  [{:keys [scalars] :as state}]
  (let [[path n] (first scalars)
        [a b :as np] (new-pair n)
        [lp rp] (map (partial conj path) [0 1])]
    (cond-> state
      path (-> (update :nbr assoc-in path np)
               (update :pairs add-new-pair path np)
               (update :scalars dissoc path)
               (update :scalars add-new-scalar lp a)
               (update :scalars add-new-scalar rp b)))))


(defn split-l10 [[nbr exploded?]]
  (cond-> nbr
    (not exploded?) split-l10*))


(defn reduction-step [nbr]
  (-> nbr
      explode-lnp
      split-l10))


(defn needs-reduction? [{:keys [scalars pairs]}]
  (or (seq scalars)
      (seq pairs)))


(defn reduce-nbr [nbr]
  (->> nbr
       (iterate reduction-step)
       (drop-while needs-reduction?)
       first
       :nbr))


(def sum-nbrs (comp reduce-nbr ->nbr vector))


(def sum-all-nbrs (partial reduce sum-nbrs))


(defn all-pairs [nbrs]
  (for [x nbrs y nbrs :when (not= x y)] [x y]))


(defn largest-sum [nbrs]
  (->> nbrs
       all-pairs
       (map (comp magnitude sum-all-nbrs))
       (apply max)))

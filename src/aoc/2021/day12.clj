(ns aoc.2021.day12)

(defn parse-nodes
  [lines]
  (->> lines
       (map #(re-matches #"(\w+)-(\w+)" %))
       (reduce
        (fn [m [_ src tgt]]
          (let [reversed? (or (= "end" src) (= "start" tgt))
                [src tgt] (if reversed?
                            [tgt src]
                            [src tgt])]
            (println "2" src "and" tgt)
            (cond-> m
              (and (not= "start" src)
                   (not= "end" tgt)) (update tgt conj src)
              :always (update src conj tgt))))
        {})))


(defn small-cave?
  [cave]
  (re-matches #"[a-z]+" cave))


(defn new-paths-single
  [path adjacent]
  (let [visited? (set path)]
    (->> adjacent
         (filter #(or (not (small-cave? %))
                      (not (visited? %))))
         (map (partial conj path)))))


(defn new-paths-twice
  [path adjacent]
  (let [visited? (set path)
        twice (->> path
                   frequencies
                   (filter (comp small-cave? first))
                   (filter #(< 1 (second %))))]
    (->> adjacent
         (filter #(or (not (small-cave? %))
                        (not (visited? %))
                        (empty? twice)))
         (map (partial conj path)))))


(defn find-paths
  ([lines] (find-paths new-paths-single lines))
  ([new-paths lines]
   (let [graph (parse-nodes lines)]
     (loop [pending '(["start"])
            paths []]

       (let [path (peek pending)
             pending (cond-> pending path pop)
             current (last path)]
         (cond
           (not path) paths
           (= "end" current) (recur pending (conj paths path))
           :else (recur
                  (into pending (new-paths path (graph current)))
                  paths)))))))


(defn path-count
  [lines]
  (->> lines
       find-paths
       count))

(defn path-count-twice
  [lines]
  (->> lines
       (find-paths new-paths-twice)
       count))

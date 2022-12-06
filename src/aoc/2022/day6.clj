(ns aoc.2022.day6)

(defn- marker? [n [_ s]]
  (= n (count (set s))))

(defn find-marker
  ([s] (find-marker 4 s))
  ([n s]
   (->> s
        (partition n 1)
        (map-indexed vector)
        (filter (partial marker? n))
        ffirst
        (+ n))))

(def find-message (partial find-marker 14))

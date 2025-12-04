(ns aoc.2025.day01)

(defn rotate-dial [dial [dir distance]]
  (let [f (if (= \L dir) - +)]
    (mod (f dial distance) 100)))


(defn calc-password [instructions]
  (->> instructions
       (reductions rotate-dial 50)
       (drop 1)
       (filter zero?)
       count))


(defn times-crossing-zero [[dial] [dir distance]]
  (let [f (if (= \L dir) - +)
        full-round (quot distance 100)
        actual-dist (mod distance 100)
        new-dial (f dial actual-dist)
        result (mod new-dial 100)
        total-across (cond-> full-round
                       (and (zero? result) (pos? actual-dist)) inc
                       (and (pos? result)
                            (or
                             (and (neg? new-dial) (pos? dial))
                             (<= 100 new-dial))) inc)]
    [result total-across]))


(defn calc-password-0x434C49434B [instructions]
  (->> instructions
       (reductions times-crossing-zero [50 0])
       (drop 1)
       (map second)
       (apply +)))

(ns aoc.2020.day25)


(defn transform [subject val]
   "Set the value to itself multiplied by the subject number.
    Set the value to the remainder after dividing the value by 20201227."
  (rem (* val subject) 20201227))


(defn find-loop-cycle [pk]
  (->> 1
       (iterate (partial transform 7))
       (take-while (partial not= pk))
       count))

(defn transform-times [pk cycle]
  (->> 1
       (iterate (partial transform pk))
       (drop cycle)
       first))

(defn encryption-key [{:keys [card door]}]
  (->> [card door]
       (map find-loop-cycle)
       second
       (transform-times card)))

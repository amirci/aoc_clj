(ns aoc.2023.day14)


(defn move-rocks-front [rocks]
  (->> rocks
       count
       (range 0)
       (reduce
        (fn [[rocks last-idx :as acc] idx]
          (case (rocks idx)
            \. acc
            \# [rocks (inc idx)]
            \O (-> acc
                   (assoc-in [0 idx] \.)
                   (assoc-in [0 last-idx] \O)
                   (update 1 inc))))
        [rocks 0])
       first))


(defn tilt-west [platform]
  (->> platform
       (map move-rocks-front)))

(defn tilt-north [platform]
  (->> platform
       (apply mapv vector)
       (map move-rocks-front)
       (apply mapv vector)))


(defn tilt-south [platform]
  (->> platform
       reverse
       tilt-north
       reverse))


(def rock? (partial = \O))

(defn count-rocks [platform]
  (->> platform
       (map (comp count (partial filter rock?)))
       (apply +)
       ))

(defn tilt-east [platform]
  (->> platform
       (mapv (comp vec (partial into ())))
       tilt-west
       (mapv (comp vec (partial into ()))))
  )

(defn rock-weight [total [idx beam]]
  (->> beam
       (filter (partial = \O))
       count
       (* (- total idx))))

(defn sum-rock-weights [platform]
  (->> platform
       (map-indexed vector)
       (map (partial rock-weight (count platform)))
       (apply +)))

(defn sum-tilted-rocks [platform]
  (->> platform
       tilt-north
       sum-rock-weights))


(defn tilt-cycle [platform]
  (let [total (count-rocks platform)
        tilted (->> platform
                    tilt-north
                    tilt-west
                    tilt-south
                    tilt-east)]
    (assert (= total (count-rocks tilted)))
    tilted))

(defn cycle-platform [platform]
  (iterate tilt-cycle platform))

(defn cycle-for [n platform]
  (->> platform
       cycle-platform
       (drop n)
       first))

(defn step [[platform freq idx]]
  (let [platform (tilt-cycle platform)]
    [platform
     (update freq platform (fnil inc 0))
     (inc idx)]))


(defn no-repeat [n [_ freq]]
  (-> freq vals set (contains? n) not))


(defn cyle-until-repeat [n state]
  (->> state
       (iterate step)
       (drop-while (partial no-repeat n))
       first))


(defn sum-tilted-cycle [n platform]
  (let [[_ _ idx1 :as state1] (cyle-until-repeat 2 [platform {} 0])
        [_ _ idx2 :as state2] (cyle-until-repeat 3 state1)
        rst (mod (- n idx1) (- idx2 idx1))]
    (->> state2
         (iterate step)
         (drop rst)
         ffirst
         sum-rock-weights)))

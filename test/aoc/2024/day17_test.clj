(ns aoc.2024.day17-test
  (:require [aoc.2024.day17 :as sut]
            [clojure.test :as t]
            [clojure.math :as math]))


(def input
  {:A 27334280
   :B 0
   :C 0
   :program [2,4,1,2,7,5,0,3,1,7,4,1,5,5,3,0]})



(def sample
  {:A 729
   :B 0
   :C 0
   :ptr 0
   :program [0,1,5,4,3,0]})


#_(t/deftest run-instruction-test
  (t/is (= {:A 0 :B 1 :C 9 :program [2 6] :ptr 2}
           (sut/run-instruction {:A 0 :B 0 :C 9 :program [2 6] :ptr 0})))
  (t/is (= {:A 10 :B 0 :C 0 :output [0,1,2]
            :program [5,0,5,1,5,4] :ptr 6 }
           (sut/run-til-halts {:A 10 :B 0 :C 0 :program [5,0,5,1,5,4]})))
  (t/is (= {:A 0 :B 0 :C 0 :output [4,2,5,6,7,7,7,7,3,1,0]
            :program [0,1,5,4,3,0] :ptr 6 }
           (sut/run-til-halts {:A 2024 :B 0 :C 0 :program [0,1,5,4,3,0]})))
  (t/is (= {:A 0 :B 26 :C 0 :output []
            :program [1 7] :ptr 2 }
           (sut/run-til-halts {:A 0 :B 29 :C 0 :program [1 7]})))
  (t/is (= {:A 0 :B 44354 :C 43690 :output []
            :program [4 0] :ptr 2 }
           (sut/run-til-halts {:A 0 :B 2024 :C 43690 :program [4 0]}))))


(t/deftest part-1
  (t/is (= 1 (->> {:A 0 :B 0 :C 9 :program [2 6]} sut/run-til-halts :B)))
  (t/is (= [4,6,3,5,6,3,5,2,1,0] (->> sample sut/run-til-halts :output)))
  (t/is (= [7,6,5,3,6,5,7,0,4] (->> input sut/run-til-halts :output))))

(def sample-2
  {:A 2024
   :B 0
   :C 0
   :program [0,3,5,4,3,0]})

(t/deftest part-2
  (t/is (= [0 3 5 4 3 0] (->> (assoc sample-2 :A 117440)
                              sut/run-til-halts
                              :output)))
  (t/is (= 190615597431823 (->> input sut/find-number-matching-program))) )


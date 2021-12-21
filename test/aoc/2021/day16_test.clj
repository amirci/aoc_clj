(ns aoc.2021.day16-test
  (:require [aoc.2021.day16 :as sut]
            [clojure.string :as st]
            [clojure.test :as t :refer [deftest is]]))

(def input
  (-> "resources/2021/day16.txt"
      slurp
      st/split-lines
      first))


(deftest part-1
  (let [[{:keys [packets version]}] (sut/parse-packets "38006F45291200")]
    (is (= 1 version))
    (is (= [10 20] (map :value packets))))
  (let [[{:keys [packets]} & rst] (sut/parse-packets "EE00D40C823060")]
    (is (= 1 (count rst)))
    (is (= [1 2 3] (map :value packets))))
  (is (= 16 (sut/sum-versions "8A004A801A8002F478")))
  (is (= 12 (sut/sum-versions "620080001611562C8802118E34")))
  (is (= 23 (sut/sum-versions "C0015000016115A2E0802F182340")))
  (is (= 31 (sut/sum-versions "A0016C880162017C3686B18A3D4780")))
  (is (= 897 (sut/sum-versions input))) )



(deftest part-2
  (is (= 3 (:value (first (sut/parse-packets "C200B40A82")))))
  (is (= 54 (:value (first (sut/parse-packets "04005AC33890")))))
  (is (= 9485076995911 (:value (first (sut/parse-packets input))))))





(ns aoc.2017.day15-test
  (:require [aoc.2017.day15 :as sut]
            [clojure.test :refer [deftest testing is]]))



(def sample {:a 65 :b 8921})


(def input {:a 873 :b 583})


(def fortym 40000000)

(deftest part-a
  (testing "sample generators"
    (is (= [1092455 1181022009 245556042 1744312007 1352636452]
           (->> (sut/factors :a)
                (sut/mk-gen (sample :a))
                (take 5))
           ))

    (is (= [430625591 1233683848 1431495498 137874439 285222916]
           (->> (sut/factors :b)
                (sut/mk-gen (sample :b))
                (take 5))
           )))

  (testing "sample with 5"
    (is (= 1 (sut/count-matching sample 5))))

  (testing "sample with 40m"
    (is (= 588 (sut/count-matching sample fortym))))

  (testing "input with 40m"
    (is (= 631 (sut/count-matching input fortym)))))


(deftest part-b
  (testing "sample generators"
    (is (= [1352636452 1992081072 530830436 1980017072 740335192]
           (->> (sut/factors :a)
                (sut/mk-gen-picky 4 (sample :a))
                (take 5))
           ))

    (is (= [1233683848 862516352 1159784568 1616057672 412269392]
           (->> (sut/factors :b)
                (sut/mk-gen-picky 8 (sample :b))
                (take 5))
           )))

  (testing "sample with 5"
    (is (= 1 (sut/count-matching-picky sample 1056))))

  (testing "sample with 5m"
    (is (= 309 (sut/count-matching-picky sample 5000000))))

  (testing "input with 5m"
    (is (= 279 (sut/count-matching-picky input 5000000)))) )




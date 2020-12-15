(ns aoc.2020.day15-test
  (:require [aoc.2020.day15 :as sut]
            [clojure.test :as t :refer :all]))


(def input [10,16,6,0,1,17])

(def init-sample (sut/init [0 3 6]))

(def ->test #(select-keys % [:turn :last]))

(deftest part-a
  (testing "samples"
    (is (= {:turn 2 :last 0}
           (->test (sut/speak-next init-sample))))

    (is (= [0 3 3 1 0 4 0]
           (->> (range 4 11)
                (map (partial sut/play [0 3 6])))))

    (is (= 436 (sut/play [0 3 6] 2020)))
    (is (= 1 (sut/play [1 3 2] 2020)))
    (is (= 10 (sut/play [2 1 3] 2020)))
    (is (= 27 (sut/play [1 2 3] 2020))))

  (testing "input"
    (is (= 412 (sut/play input 2020)))))

(def turn-b 30000000)

#_(deftest part-b
  (testing "samples"
    (is (= 175594 (sut/play [0 3 6] turn-b))))
  (testing "input"
    (is (= 243 (sut/play input turn-b)))))


(ns aoc.2020.day23-test
  (:require [aoc.2020.day23 :as sut]
            [flames.core :as flames]
            [clojure.test :refer :all]))

(def input "952438716")

(def sample "389125467")

(def sample-cups (-> sample
                     sut/->digits
                     sut/mk-map))

(deftest part-a
  (testing "move-cups"
    (let [{:keys [cups idx picked] :as moved} (->> sample-cups sut/move-cups)]
      (is (= "289154673" (sut/->cups moved)))
      (is (= [8 9 1] picked))))

  (testing "sample"
    (is (= "192658374" (sut/play 10 sample)))
    (is (= "167384529" (sut/play 100 sample))))

  (testing "input"
    (is (= "197342568" (sut/play 100 input)))))

(def ten-mill 10000000)

(deftest ^:slow part-b
  (testing "sample"
    (is (= 149245887792 (sut/play-mill ten-mill sample))))
  (testing "input"
    (is (= 902208073192 (sut/play-mill ten-mill input))))) 


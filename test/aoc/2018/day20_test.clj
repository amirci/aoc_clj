(ns aoc.2018.day20-test
  (:require  
    [clojure.test :refer :all]
    [blancas.kern.core :as k]
    [aoc.2018.day20 :as dut]))


(def input
  (-> "resources/2018/day20.input.txt"
      slurp))

(defn parse
  [s]
  (->> s
       (k/value dut/regexp)
       (map (partial apply str))
       set))

(deftest sample-tests
  (is (= #{"ENWWWNEEE" "ENWWWSSEEE" "ENWWWSSEN"}
         (parse "^ENWWW(NEEE|SSE(EE|N))$"))))

(deftest further-room-sample-tests
  (is (= 23
         (dut/furthest-room "^ESSWWN(E|NNENN(EESS(WNSE|)SSS|WWWSSSSE(SW|NNNE)))$")))
  (is (= 31
         (dut/furthest-room "^WSSEESWWWNW(S|NENNEEEENN(ESSSSW(NWSW|SSEN)|WSWWN(E|WWS(E|SS))))$"))))


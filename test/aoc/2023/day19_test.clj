(ns aoc.2023.day19-test
  (:require [aoc.2023.day19 :as sut]
            [clojure.string :as s]
            [blancas.kern.core :as kc]
            [clojure.test :refer [deftest is are] :as t]))

(def input
  (->> "resources/2023/day19.txt"
       slurp
       s/split-lines
       (partition-by empty?)
       ((juxt first last))))



(def sample
  [["px{a<2006:qkq,m>2090:A,rfg}"
    "pv{a>1716:R,A}"
    "lnx{m>1548:A,A}"
    "rfg{s<537:gd,x>2440:R,A}"
    "qs{s>3448:A,lnx}"
    "qkq{x<1416:A,crn}"
    "crn{x>2662:A,R}"
    "in{s<1351:px,qqz}"
    "qqz{s>2770:qs,m<1801:hdj,R}"
    "gd{a>3333:R,R}"
    "hdj{m>838:A,pv}"]
   ["{x=787,m=2655,a=1222,s=2876}"
    "{x=1679,m=44,a=2067,s=496}"
    "{x=2036,m=264,a=79,s=2244}"
    "{x=2461,m=1339,a=466,s=291}"
    "{x=2127,m=1623,a=2188,s=1013}"]])


(deftest predicate-target-p-test
  (are [s state exp-state]
      (let [f (kc/value sut/predicate-target-p s)]
        (is (= exp-state (f state))))
    "a<2006:qkq" {:part {:a 3}} {:part {:a 3} :next "qkq"}
    "s>2006:qkq" {:part {:s 3}} nil
    "m=20:A" {:part {:m 20 :x 1}} {:part {:m 20 :x 1}
                                   :next nil
                                   :accepted [{:m 20 :x 1}]}
    "s<30:R" {:part {:s 28 :x 1}} {:part {:s 28 :x 1}
                                   :next nil
                                   :rejected [{:s 28 :x 1}]}))


(deftest rule-ptest
  (let [[id f] (kc/value sut/rule-p "ddj{s<2598:fr,x>1640:pkr,sm}")]
    (is (= "ddj" id))
    (is (= {:next "fr" :part {:s 2500 :x 2000}} (f {:part {:s 2500 :x 2000}})))
    (is (= {:next "pkr" :part {:s 3000 :x 1700}} (f {:part {:s 3000 :x 1700}})))
    (is (= {:next "sm" :part {:s 3000 :x 1000}} (f {:part {:s 3000 :x 1000}}))))

  (let [[id f] (kc/value sut/rule-p "qqz{s>2770:qs,m<1801:hdj,R}")]
    (is (= "qqz" id))
    (is (= {:next "qs" :part {:s 3000 :m 2000}}
             (f {:part {:s 3000 :m 2000}})))
    (is (= {:next "hdj" :part {:s 2000 :m 1000}}
           (f {:part {:s 2000 :m 1000}})))
    (is (= {:next nil :part {:s 2000 :m 2000}
            :rejected [{:s 2000 :m 2000}]}
           (f {:part {:s 2000 :m 2000}})))))


(deftest part1-test
  (is (= 19114 (sut/sum-accepted-parts sample)))
  (is (= 399284 (sut/sum-accepted-parts input))))


(deftest range-pred-list-test
  (let [p (kc/value sut/range-pred-list-p "{s<1351:px,qqz}")]
    (is (= [{:x [1 4000], :m [1 4000], :a [1 4000], :s [1 1350], :next "px"}
            {:x [1 4000], :m [1 4000], :a [1 4000], :s [1351 4000], :next "qqz"}]
           (p sut/default-ratings)))))

(deftest part2-test
  (is (= 167409079868000 (sut/all-combinations (sample 0))))
  (is (= 121964982771486 (sut/all-combinations (input 0))))
  )


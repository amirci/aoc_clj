(ns aoc.2020.day19-test
  (:require [aoc.2020.day19 :as sut]
            [clojure.java.io :as io]
            [blancas.kern.core :refer :all]
            [clojure.test :as t :refer :all]))

(def input
  (->> "2020/day19.input.txt"
       io/resource
       slurp
       clojure.string/split-lines
       (split-with seq)))


(def sample
  ["0: 1 2"
   "1: \"a\""
   "2: 1 3 | 3 1"
   "3: \"b\""])

(def sample-2
  [["0: 4 1 5"
    "1: 2 3 | 3 2"
    "2: 4 4 | 5 5"
    "3: 4 5 | 5 4"
    "4: \"a\""
    "5: \"b\""]
   ["ababbb"
    "bababa"
    "abbbab"
    "aaabbb"
    "aaaabbb"]])

(deftest part-a
  (testing "sample"
    (is (= 2 (sut/find-matching sample-2))))

  (testing "input"
    (is (= 111 (sut/find-matching input)))))


(def sample-b
  [["0: 8 11"
    "1: \"a\""
    "2: 1 24 | 14 4"
    "3: 5 14 | 16 1"
    "4: 1 1"
    "5: 1 14 | 15 1"
    "6: 14 14 | 1 14"
    "7: 14 5 | 1 21"
    "8: 42"
    "9: 14 27 | 1 26"
    "10: 23 14 | 28 1"
    "11: 42 31"
    "12: 24 14 | 19 1"
    "13: 14 3 | 1 12"
    "14: \"b\""
    "15: 1 | 14"
    "16: 15 1 | 14 14"
    "17: 14 2 | 1 7"
    "18: 15 15"
    "19: 14 1 | 14 14"
    "20: 14 14 | 1 15"
    "21: 14 1 | 1 14"
    "22: 14 14"
    "23: 25 1 | 22 14"
    "24: 14 1"
    "25: 1 1 | 1 14"
    "26: 14 22 | 1 20"
    "27: 1 6 | 14 18"
    "28: 16 1"
    "31: 14 17 | 1 13"
    "42: 9 14 | 10 1"]
   ["abbbbbabbbaaaababbaabbbbabababbbabbbbbbabaaaa"
    "bbabbbbaabaabba"
    "babbbbaabbbbbabbbbbbaabaaabaaa"
    "aaabbbbbbaaaabaababaabababbabaaabbababababaaa"
    "bbbbbbbaaaabbbbaaabbabaaa"
    "bbbababbbbaaaaaaaabbababaaababaabab"
    "ababaaaaaabaaab"
    "ababaaaaabbbaba"
    "baabbaaaabbaaaababbaababb"
    "abbbbabbbbaaaababbbbbbaaaababb"
    "aaaaabbaabaaaaababaa"
    "aaaabbaaaabbaaa"
    "aaaabbaabbaaaaaaabbbabbbaaabbaabaaa"
    "babaaabbbaaabaababbaabababaaab"
    "aabbbbbaabbbaaaaaabbbbbababaaaaabbaaabba"]])


(def sample-b-updated
  (-> sample-b
      (update 0 sut/parse-rules)
      (update 0 merge sut/new-rules)))


(def input-updated
  (-> input
      (update 0 sut/parse-rules)
      (update 0 merge sut/new-rules)))

(deftest part-b
  (testing "sample"
    (is (= 12 (sut/find-matching* sample-b-updated))))

  (testing "input"
    (is (= 343 (sut/find-matching* input-updated)))))

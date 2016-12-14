(ns aoc.2016.day14-test
  (:require [clojure.test :refer :all]
            [digest :refer :all]
            [aoc.2016.day14 :refer :all]))

(deftest part-a-sample-test
  (testing "first and second values"
    (let [[fst snd] (take 2 (pad-key-gen "abc"))]
      (is (= [39 (digest/md5 "abc39")] fst))
      (is (= [92 (digest/md5 "abc92")] snd))))

  (testing "the 64th key"
    (let [key (nth (pad-key-gen "abc") 63)]
      (is (= [22728 (digest/md5 "abc22728")] key)))))

; rank 598
(deftest part-a
  (let [key (nth (pad-key-gen salt) 63)]
    (is (= [1 (digest/md5 (str salt 1))] key))))

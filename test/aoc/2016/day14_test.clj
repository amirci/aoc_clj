(ns aoc.2016.day14-test
  (:require [clojure.test :refer :all]
            [digest :refer :all]
            [aoc.2016.day14 :refer :all]))

(deftest part-a
  (testing "first and second values"
    (let [[fst snd] (take 2 (pad-key-gen "abc"))]
      (is (= [39 (digest/md5 "abc39")] fst))
      (is (= [92 (digest/md5 "abc92")] snd))))

  (testing "the 64th key"
    (let [key (nth (pad-key-gen "abc") 63)]
      (is (= [22728 (digest/md5 "abc22728")] key)))))

; rank 598
;(deftest part-a
;  (let [key (nth (pad-key-gen salt) 63)]
;    (is (= [23769 (digest/md5 (str salt 23769))] key))))
;
;
;(deftest part-b
;  (let [key (nth (pad-key-gen salt hash-gen-2016) 63)]
;    (is (= [20606 (digest/md5 (str salt 20606))] key))))


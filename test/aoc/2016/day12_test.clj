(ns aoc.2016.day12-test
  (:require [clojure.test :refer :all]
            [aoc.2016.day12 :refer :all]))

(def instructions
  (-> "2016/day12.input.txt"
      clojure.java.io/resource
      slurp
      clojure.string/split-lines))

(def sample-instructions
  ["cpy 41 a"
   "inc a"
   "inc a"
   "dec a"
   "jnz a 2"
   "dec a"])

(deftest sample-run
  (is (= 42 ((run-assembunny sample-instructions) "a"))))

(deftest part-a
  (let [registers (time (run-assembunny instructions))]
    (is (= 318009 (registers "a")))))

(deftest part-b
  (let [registers (time (run-assembunny (assoc init-zero "c" 1) instructions))]
    (is (= 9227663 (registers "a")))))


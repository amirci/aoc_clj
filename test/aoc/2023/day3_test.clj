(ns aoc.2023.day3-test
  (:require [aoc.2023.day3 :as sut]
            [clojure.string :as s]
            [clojure.test :refer [deftest is] :as t]))


(def engine-schema
  (->> "resources/2023/day3.txt"
       slurp
       s/split-lines))

(def sample
  ["467..114.."
   "...*......"
   "..35..633."
   "......#..."
   "617*......"
   ".....+.58."
   "..592....."
   "......755."
   "...$.*...."
   ".664.598.."])

(deftest part-1
  (is (= 4361 (sut/sum-part-numbers sample)))
  (is (= 551094 (sut/sum-part-numbers engine-schema))))


(deftest part-2
  (is (= 467835 (sut/sum-gear-ratios sample)))
  (is (= 80179647 (sut/sum-gear-ratios engine-schema))))


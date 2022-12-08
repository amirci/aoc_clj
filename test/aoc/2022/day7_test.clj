(ns aoc.2022.day7-test
  (:require [aoc.2022.day7 :as sut]
            [clojure.string :as s]
            [clojure.test :refer [deftest is] :as t]))

(def console-output
  (->> "resources/2022/day7.txt"
       slurp
       s/split-lines))

(def sample
  (->> ["$ cd /"
        "$ ls"
        "dir a"
        "14848514 b.txt"
        "8504156 c.dat"
        "dir d"
        "$ cd a"
        "$ ls"
        "dir e"
        "29116 f"
        "2557 g"
        "62596 h.lst"
        "$ cd e"
        "$ ls"
        "584 i"
        "$ cd .."
        "$ cd .."
        "$ cd d"
        "$ ls"
        "4060174 j"
        "8033020 d.log"
        "5626152 d.ext"
        "7214296 k"]))

(deftest part-1
  (is (= 95437 (sut/find-sum-dirs sample)))
  (is (= 1348005 (sut/find-sum-dirs console-output)))
  )

(deftest part-2
  (is (= 24933642 (sut/find-min-dir-to-free sample)))
  (is (= 12785886 (sut/find-min-dir-to-free console-output))))

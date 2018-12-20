(ns aoc.2018.day19
  (:require
    [clojure.tools.trace :refer [trace]]
    [aoc.2018.day16 :as d16]
    [clojure.string :as str]))

(def ns16 (find-ns 'aoc.2018.day16))

(defn set-ptr-reg
  [[ptr ptr-reg regs] n _ _]
  [ptr n regs])

(defn parse-instruction
  [[op a b c :as inst]]
  (let [v (or (ns-resolve ns16 op) set-ptr-reg)
        f (if (var? v) @v v)]
    [f a b c]))

(defn parse-program
  [input]
  (->> (clojure.string/replace input "#ip" "set-ptr-reg")
       clojure.string/split-lines
       (map #(str "[" % "]"))
       (map read-string)
       (map parse-instruction)))

(defn run-instruction
  [[ptr ptr-reg regs [f a b c]]]
  (f [ptr ptr-reg regs] a b c))

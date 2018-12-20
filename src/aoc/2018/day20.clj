(ns aoc.2018.day20
  (:require
    [clojure.tools.trace :refer [trace]]
    [blancas.kern.core :as k :refer :all]
    [blancas.kern.lexer.java-style :refer :all]
    [clojure.string :as str]))


(def move-dir (one-of "NSWE"))

(value move "E")

(^ENWWW(NEEE|SSE(EE|N))$)


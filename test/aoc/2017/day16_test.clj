(ns aoc.2017.day16-test
  (:require [aoc.2017.day16 :as sut]
            [clojure.java.io :as io]
            [blancas.kern.core :as kc :refer [value]]
            [clojure.test :refer [deftest testing is]]))

(def input
  (->> "2017/day16.input.txt"
       io/resource
       slurp
       clojure.string/split-lines
       first
       (#(clojure.string/split % #","))))



(def sample (vec "abcde"))

(def programs (vec "abcdefghijklmnop"))

(deftest part-a
  (testing "spin"
    (is (= [\e \a \b \c \d] (sut/spin 1 sample)))
    (is (= [\e \a \b \c \d] ((value sut/spin-expr "s1") sample))))

  (testing "exchange"
    (is (= [\a \b \d \c \e] (sut/exchange 2 3 sample)))
    (is (= [\a \b \d \c \e] ((value sut/exchange-expr "x2/3") sample))))

  (testing "swap"
    (is (= [\c \b \a \d \e] (sut/swap \a \c sample)))
    (is (= [\c \b \a \d \e] ((value sut/swap-expr "pa/c") sample))))

  (testing "sample"
    (is (= "baedc" (sut/dance sample ["s1" "x3/4" "pe/b"]))))

  (testing "input"
    (is (= "eojfmbpkldghncia" (sut/dance programs input)))))


(def oneb 1000000000)

(deftest part-b
  (testing "input"
    (is (= "iecopnahgdflmkjb" (sut/dance oneb programs input)))))


(ns aoc.2022.day11
  (:require [blancas.kern.core :as kc]
            [blancas.kern.lexer.basic :as lex]
            [clojure.string :as s]))

(defn phrase-p [s p]
  (kc/skip-ws
   (kc/>> (lex/word s) p)))

(def items-p
  (phrase-p "Starting items:" (lex/comma-sep1 lex/dec-lit)))

(def lit-or-old-p
  (kc/<|>
   (kc/<$> #(constantly %) lex/dec-lit)
   (kc/<$> (constantly identity) (lex/token "old"))))

(def plus-or-mul-p
  (kc/<|>
   (kc/<$> (constantly +) (lex/token "+"))
   (kc/<$> (constantly *) (lex/token "*"))))

(def op-p
  (kc/skip-ws
   (kc/bind [_ (lex/token "Operation: new = old")
             f plus-or-mul-p
             exp-f lit-or-old-p]
            (kc/return (fn [old] (f old (exp-f old)))))))

(defn phrase-num-p [s] (phrase-p s lex/dec-lit))

(def test-div-p (phrase-num-p "Test: divisible by"))

(def true-p (phrase-num-p "If true: throw to monkey"))

(def false-p (phrase-num-p "If false: throw to monkey"))

(def test-p (kc/<*> test-div-p true-p false-p))

(def monkey-p
  (kc/<$>
   (partial zipmap [:items :op :test])
   (kc/<*> items-p op-p test-p)))

(defn parse-monkey [lines]
  (kc/value monkey-p (s/join (rest lines))))

(defn a-bit-worried [wl] (int (/ wl 3)))

(defn monkey-inspect
  "new-wl = Call op with wl
   new-wl /= 3
   if new-wl is divisible by div-by then add it to monkey mk-true
   else add it to the items queue of mk-false"
  [handle-worry op [div-by mk-true mk-false] mks wl]
  (let [new-wl (handle-worry (op wl))
        tgt-mk (if (zero? (mod new-wl div-by)) mk-true mk-false)]
    (update-in mks [tgt-mk :items] conj new-wl)))

(defn monkey-round
  "For every worry level in the items queue, call monkey-inspect
  with the wl, operation, test and monkeys to update"
  [handle-worry mks idx]
  (println (class mks))
  (let [{:keys [op test items]} (mks idx)
        inspect (partial monkey-inspect handle-worry op test)]
    (println "Going to reduce")
    (reduce
     inspect
     #_(fn [mks wl] (monkey-inspect handle-worry wl op test mks))
     (-> mks
         (assoc-in [idx :items] [])
         (update-in [idx :mb] (fnil + 0) (count items)))
     items)))

(defn keep-away-round
  "For every monkey, remove one worry level from the items queue and
  call monkey-inspect with the worry level, operation and test"
   [handle-worry monkeys]
  (reduce
   (partial monkey-round handle-worry)
   monkeys
   (range (count monkeys))))

(defn- parse-all-monkeys [input]
  (->> input
       (map parse-monkey)
       vec))

(defn busiest-monkey-business
  [handle-worry times input]
  (let [monkeys (parse-all-monkeys input)
        worry-f (handle-worry monkeys)]
    (->> monkeys
         (iterate (partial keep-away-round worry-f))
         (drop times)
         first
         (map :mb)
         (sort >)
         (take 2)
         (apply *))))

(def busiest-mb-a-bit-worried
  (partial busiest-monkey-business
     (constantly a-bit-worried)
     20))

(defn very-worried [gcd wl] (mod wl gcd))

(defn- calc-gcd [monkeys]
  (->> monkeys
       (map (comp first :test))
       (apply *)))

(defn gcd-adjust-worry [monkeys]
  (partial very-worried (calc-gcd monkeys)))

(def busiest-mb-worried
  (partial busiest-monkey-business
     gcd-adjust-worry
     10000))

(ns aoc.2023.day18
  (:require [blancas.kern.core :as kc :refer [<$> <*> many1 >> hex-digit]]
            [blancas.kern.lexer.basic :as lex :refer [dec-lit token parens]]))

(def dir->dig
  {"D" [1 0] "L" [0 -1] "U" [-1 0] "R" [0 1]})


(def hex-color-p
  (parens (>> (token "#") (many1 hex-digit))))


(def dir-p
  (<$> dir->dig (token "D" "L" "R" "U")))


(def dig-instruction-p
  (<*> dir-p dec-lit hex-color-p))


(def color->dir
  {\1 [1 0] \2 [0 -1] \3 [-1 0] \0 [0 1]})


(defn color->instruction [color]
  (let [hex (subvec color 0 5)
        dir (color 5)]
    (vector
     (color->dir dir)
     (->> hex (apply str) (str "0x") read-string))))


(def instruction-color-p
  (<$> (fn [color] (color->instruction color))
       (>> dir-p dec-lit hex-color-p)))

(defn parse [instruction]
  (kc/value dig-instruction-p instruction))

(defn parse-color [instruction]
  (kc/value instruction-color-p instruction))


(defn picks-formula [parsed]
  (->> parsed
       (reduce (fn [[perim area pos] [dir len]]
                 (let [[dy dx] (map * dir [len len])
                       [y x] (map + pos [dy dx])]
                   [(+ perim len)
                    (+ area (* x dy))
                    [y x]]))
               [0 0 [0 0]])
       ((fn [[perim area]]
           (+ area (quot perim 2) 1)))))

(defn lava-lake-area
  ([instructions] (lava-lake-area parse instructions))
  ([parse instructions]
   (->> instructions (map parse) picks-formula)))


(defn lava-lake-area-2 [instructions]
  (lava-lake-area parse-color instructions))

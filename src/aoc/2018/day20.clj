(ns aoc.2018.day20
  (:require
    [clojure.tools.trace :refer [trace]]
    [blancas.kern.core :as k :refer :all]
    [blancas.kern.lexer.java-style :refer :all]
    [clojure.string :as str]))

; movedir  := one-of NWES
; move-seq := many1 move-dir
; list-of-re := sep-end-by1 \| regexp*
; regexp*  := move-seq (optional (paren list-of-re))
; regexp   := (sym* ^) regexp* (sym* $)

(def move-dir (one-of "NSWE"))

(def move-seq (many1 move-dir))

(declare regexp*)

(def list-of-re
 (bind [re-list (sep-end-by1 (sym* \|) (fwd regexp*))]
       (return (apply concat re-list))))

(def regexp* 
  (bind [re move-seq re-list (optional (parens list-of-re))]
        (let [re-list (case (count re-list)
                        0 [[]]
                        1 (conj re-list [])
                        re-list)
              re* (for [l re-list] (concat re l))]
          (return re*))))

(def regexp
 (bind [_ (sym* \^) re regexp* _ (sym* \$)]
       (return re)))

;^ENWWW(NEEE|SSE(EE|N))$

(value regexp "^ENWWW$")
(value regexp "^ENWWW(NEEE|)$")
(value regexp "^ENWWW(NEEE|SSE)$")
(value regexp "^ENWWW(NEEE|SSE(EE|N))$")


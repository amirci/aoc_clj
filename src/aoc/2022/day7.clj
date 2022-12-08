(ns aoc.2022.day7
  (:require [blancas.kern.core :as kc]
            [blancas.kern.lexer.basic :as lex]))

(def empty-root {:type :dir :size 0 :children {} :dir-name :root})

(defn parse-and-update-state
  "Takes a parser and a function that received the result
  of the parser and the state and returns a new state"
  [parser f]
  (kc/bind [result parser
            _ (kc/modify-state f result)
            state kc/get-state]
           (kc/return state)))

(def dir-pp
  (kc/<$>
   #(assoc empty-root :dir-name %)
   (kc/>> (lex/word "dir") lex/identifier)))

(def dir-p
  (parse-and-update-state
   dir-pp
   (fn [[path :as state] {:keys [dir-name] :as dir}]
     (let [full-path (conj path dir-name)]
       (update state 1 assoc-in full-path dir)))))

(def ext-p
  (kc/>> lex/dot lex/identifier))

(def file-pp
  (kc/<$>
   (fn [[_ file-name ext :as input]]
     (-> [:size :file-name :ext]
         (zipmap input)
         (assoc :type :file :full-name (str file-name ext))))
   (kc/<*> lex/dec-lit
           lex/identifier
           (kc/optional ext-p))))

(def file-p
  (parse-and-update-state
   file-pp
   (fn [[path :as state] {:keys [full-name] :as file}]
     (let [full-path (conj path full-name)]
       (update state 1 assoc-in full-path file)))))

(def root-p
  (parse-and-update-state
   (kc/sym* \/)
   (fn [state _] (assoc state 0 [:children]))))


(def prev-p
  (parse-and-update-state
   (lex/token "..")
   (fn [state _] (update state 0 (comp vec (partial drop-last 2))))))

(def change-dir-p
  (parse-and-update-state
   lex/identifier
   #(update %1 0 conj %2 :children)))

(def cd-p
  (kc/>> (lex/token "cd")
          (kc/<|> root-p
                  prev-p
                  change-dir-p)))

(def ls-p
  (parse-and-update-state
   (lex/token "ls")
   (fn [state _] state)))

(def cmd-p
  (kc/>> (lex/sym \$) (kc/<|> cd-p ls-p)))


(def console-p
  (kc/<|> cmd-p file-p dir-p))

(defn mk-dir-structure [state line]
  (kc/value console-p line "" state))

(def dir? :children)

(declare update-folder-size)

(defn update-children-size [children]
  (reduce-kv
   (fn [m k v]
     (assoc m k
            (if (dir? v)
              (update-folder-size v)
              v)))
   {}
   children))

(defn- update-size [folder]
  (assoc folder
         :size
         (->> folder
              :children
              (map (comp :size second))
              (apply +))))

(defn- update-folder-size
  "Takes a folder with :children, updates each
  child folder :size and then calculates the :size
  for the `folder`"
  [folder]
  (-> folder
      (update :children update-children-size)
      update-size))

(defn- find-folders [pred {:keys [size children] :as folder}]
  (->> children
       (map second)
       (filter dir?)
       (mapcat (partial find-folders pred))
       (#(cond-> %
           (pred size) (conj (select-keys folder [:dir-name :size]))
           ))))

(defn- folders-at-most [target folder]
  (find-folders #(<= % target) folder))

(defn- load-console-output [lines]
  (->> lines
       (reduce mk-dir-structure [[:children] empty-root])
       second
       update-folder-size))

(defn find-sum-dirs [lines]
  (->> lines
       load-console-output
       (folders-at-most 100000)
       (map :size)
       (apply +)))

(def total 70000000)

(def min-free 30000000)

(defn- candidates-to-free [{:keys [size] :as root}]
  (let [free (- total size)]
    (find-folders #(<= min-free (+ free %)) root)))

(defn find-min-dir-to-free [lines]
  (->> lines
       load-console-output
       candidates-to-free
       (sort-by :size)
       first
       :size))

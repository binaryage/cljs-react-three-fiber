(ns react-three-fiber.examples.lib.styled-components
  (:require [shadow.resource :as rc]
            [clojure.string :as string]))

(defn post-process-css-text
  "Intellij Idea has some support for scss but styled-component allows superset of this syntax.
   To avoid IDE warnings we allow some unsupported css code to be put into comments '//- '
   and we remove it here."
  [text]
  (string/replace text #"\/\/- " ""))

(defmacro inline-css [path]
  (assert (string? path))
  (-> (rc/slurp-resource &env path)
      (post-process-css-text)))

(declare simple-css-transform)

(defn css-transformer [[k v]]
  (if (keyword? k)
    [(name k) ":" (name v) ";"]
    [k " " "{" (simple-css-transform v) "}"]))

(defn simple-css-transform [css-data]
  (assert (map? css-data))
  (mapcat css-transformer css-data))

(defn emit-simple-css [css-data]
  (-> (simple-css-transform css-data)
      (flatten)
      (string/join)))

(defmacro simple-css [css-data]
  (emit-simple-css css-data))

(comment
  (simple-css {:position "absolute"
               :width    "250px"})

  (simple-css {:display :block
               "a"      {:position "absolute"
                         :width    "250px"}
               'h1      {:background-color :red}}))


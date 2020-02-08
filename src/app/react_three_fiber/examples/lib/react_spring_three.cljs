(ns react-three-fiber.examples.lib.react-spring-three
  (:require ["react-spring/three" :refer [useSprings a useSpring useTransition]]
            [goog.object :as gobj]))

(defn use-springs [length props]
  (useSprings length props))

(defn use-spring [props]
  (useSpring props))

(defn use-transition [input key-transform config]
  (useTransition input key-transform config))

(defn animated [c]
  (let [cname (name c)
        c (gobj/get a cname)]
    (assert c (str "unknown animated three.js component '" cname "'"))
    c))

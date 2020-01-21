(ns react-three-fiber.examples.lib.react-spring-three
  (:require ["react-spring/three" :refer [useSprings a]]
            [goog.object :as gobj]))

(defn use-springs [length props]
  (useSprings length props))

(defn animated [c]
  (let [cname (name c)
        c (gobj/get a cname)]
    (assert c (str "unknown animated three.js component '" cname "'"))
    c))
